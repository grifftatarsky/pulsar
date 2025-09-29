package com.gpt.pulsarproducer.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Component
@Validated
@ConfigurationProperties("nimbus.pulsar.producer")
public class PulsarConfig
{
    private String equipmentTopic;
    private String equipmentIdentifierTopic;
    private String equipmentTypeIdentifierTopic;

    private String adminService;
}
