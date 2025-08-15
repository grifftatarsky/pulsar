package com.gpt.pulsarproducer.ws;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer
{
    private final MetricsWebSocketHandler handler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry reg)
    {
        // ALARM: don't let this be commited! THIS IS A DEMO!
        reg.addHandler(handler, "/ws/metrics").setAllowedOrigins("*");
    }
}
