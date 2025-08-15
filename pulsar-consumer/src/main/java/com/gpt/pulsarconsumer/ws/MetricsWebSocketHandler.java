package com.gpt.pulsarconsumer.ws;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.gpt.pulsarconsumer.domain.StationType;
import com.gpt.pulsarconsumer.service.MetricsService;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

@Component
@RequiredArgsConstructor
public class MetricsWebSocketHandler extends org.springframework.web.socket.handler.TextWebSocketHandler
{

    private final MetricsService metrics;
    private final ObjectMapper objectMapper;
    private final Set<WebSocketSession> sessions = new CopyOnWriteArraySet<>();

    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) throws Exception
    {
        sessions.add(session);
        sendSnapshotTo(session);
    }

    @Override
    public void afterConnectionClosed(
        @NonNull WebSocketSession session,
        @NonNull CloseStatus status
    )
    {
        sessions.remove(session);
    }

    public void broadcastSnapshot()
    {
        Map<String, Long> view = buildView();

        try
        {
            String json = objectMapper.writeValueAsString(view);
            TextMessage msg = new TextMessage(json);
            for (WebSocketSession s : sessions)
            {
                if (s.isOpen())
                {
                    s.sendMessage(msg);
                }
            }
        }
        catch (IOException ignored)
        {
            // TODO: Shouldn't be ignored
        }
    }

    private void sendSnapshotTo(WebSocketSession session) throws IOException
    {
        session.sendMessage(new TextMessage(objectMapper.writeValueAsString(buildView())));
    }

    private Map<String, Long> buildView()
    {
        Map<StationType, Long> snap = metrics.snapshot();
        Map<String, Long> view = new HashMap<>();

        for (StationType t : StationType.values())
        {
            Long v = snap.getOrDefault(t, 0L);
            view.put(t.name(), v == null ? 0L : v);
        }

        return view;
    }
}
