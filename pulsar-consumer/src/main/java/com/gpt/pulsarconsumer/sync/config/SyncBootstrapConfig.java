package com.gpt.pulsarconsumer.sync.config;


import com.gpt.pulsarconsumer.sync.SyncBootstrapService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class SyncBootstrapConfig
{

    private final SyncBootstrapService bootstrap;

    @Bean
    ApplicationRunner rebuildFromCompactedTopicOnStart()
    {
        return _ -> bootstrap.rebuildFromCompactedOnStart();
    }
}