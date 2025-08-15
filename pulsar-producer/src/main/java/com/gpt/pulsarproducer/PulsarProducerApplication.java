package com.gpt.pulsarproducer;


import com.gpt.pulsarproducer.config.PulsarProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(PulsarProperties.class)
public class PulsarProducerApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(PulsarProducerApplication.class, args);
    }
}
