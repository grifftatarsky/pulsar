package com.gpt.pulsarconsumer.ws;


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
    // CAN BE MADE COMMON

    private final MetricsWebSocketHandler handler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry)
    {
        // ALARM: don't let this be commited! THIS IS A DEMO!
        registry.addHandler(handler, "/ws/metrics").setAllowedOrigins("*");
    }
}
