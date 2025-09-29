package com.gpt.pulsarproducer.outbox;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.gpt.pulsarproducer.config.PulsarConfig;
import lombok.RequiredArgsConstructor;
import org.apache.pulsar.client.admin.PulsarAdmin;
import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.client.api.Schema;
import org.geolatte.geom.json.GeolatteGeomModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class PulsarProducerConfig
{
    private final ObjectMapper objectMapper;

    @Bean(destroyMethod = "close")
    public Producer<String> equipmentProducer(
        PulsarClient client,
        PulsarConfig pulsarConfig
    ) throws PulsarClientException
    {
        this.objectMapper.registerModule(new GeolatteGeomModule());
        
        return client.newProducer(Schema.STRING)
                     .topic(pulsarConfig.getEquipmentTopic())
                     .enableBatching(true)
                     .blockIfQueueFull(true)
                     .create();
    }

    @Bean(destroyMethod = "close")
    public Producer<String> equipmentIdentifierProducer(
        PulsarClient client,
        PulsarConfig pulsarConfig
    ) throws PulsarClientException
    {
        return client.newProducer(Schema.STRING)
                     .topic(pulsarConfig.getEquipmentIdentifierTopic())
                     .enableBatching(true)
                     .blockIfQueueFull(true)
                     .create();
    }

    @Bean(destroyMethod = "close")
    public Producer<String> equipmentTypeIdentifierProducer(
        PulsarClient client,
        PulsarConfig pulsarConfig
    ) throws PulsarClientException
    {
        return client.newProducer(Schema.STRING)
                     .topic(pulsarConfig.getEquipmentTypeIdentifierTopic())
                     .enableBatching(true)
                     .blockIfQueueFull(true)
                     .create();
    }

    @Bean(destroyMethod = "close")
    public PulsarAdmin pulsarAdmin(PulsarConfig pulsarConfig) throws PulsarClientException
    {
        return PulsarAdmin.builder()
                          .serviceHttpUrl(pulsarConfig.getAdminService())
                          .build();
    }
}