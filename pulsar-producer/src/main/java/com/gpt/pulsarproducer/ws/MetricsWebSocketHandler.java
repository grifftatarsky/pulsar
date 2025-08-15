package com.gpt.pulsarproducer.ws;


import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
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
    // CAN BE MADE COMMON

    private final ObjectMapper objectMapper;
    private final Set<WebSocketSession> sessions = new CopyOnWriteArraySet<>();

    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession s)
    {
        sessions.add(s);
    }

    @Override
    public void afterConnectionClosed(
        @NonNull WebSocketSession s,
        @NonNull CloseStatus st
    )
    {
        sessions.remove(s);
    }

    public void broadcastCounts(Map<String, Long> view)
    {
        try
        {
            String json = objectMapper.writeValueAsString(view);
            TextMessage msg = new TextMessage(json);

            for (WebSocketSession s : sessions)
            {
                if (s.isOpen()) s.sendMessage(msg);
            }
        }
        catch (IOException ignored)
        {
            // Probably shouldn't be doing this either...TODO?
        }
    }
}
