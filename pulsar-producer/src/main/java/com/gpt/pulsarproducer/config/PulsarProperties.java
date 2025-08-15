package com.gpt.pulsarproducer.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties(prefix = "app.pulsar")
public class PulsarProperties
{
    private String topic;
    private String subscription;
}