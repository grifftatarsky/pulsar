package com.gpt.pulsarconsumer;


import com.gpt.pulsarconsumer.config.PulsarProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.pulsar.annotation.EnablePulsar;

@EnablePulsar
@SpringBootApplication
@EnableConfigurationProperties(PulsarProperties.class)
public class PulsarConsumerApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(PulsarConsumerApplication.class, args);
    }
}