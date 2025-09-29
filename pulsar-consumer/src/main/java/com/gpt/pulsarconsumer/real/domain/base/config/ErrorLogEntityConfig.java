package com.gpt.pulsarconsumer.real.domain.base.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Component
@Validated
@ConfigurationProperties(prefix = "nimbus.entity.error-log")
public class ErrorLogEntityConfig
{
    private boolean stackTraceEnabled;
}
