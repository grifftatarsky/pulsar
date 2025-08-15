package com.gpt.pulsarconsumer.sync.config;


import com.gpt.pulsarconsumer.sync.ReconcileService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class StartupReconcileConfig
{

    private final ReconcileService reconcileService;

    @Bean
    ApplicationRunner reconcileThenTail()
    {
        return _ -> reconcileService.reconcileThenStartTailing();
    }
}