package com.gpt.pulsarproducer.outbox;


import com.gpt.pulsarproducer.config.PulsarProperties;
import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.client.api.Schema;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PulsarProducerConfig
{

    @Bean(destroyMethod = "close")
    public Producer<String> stationsChangelogProducer(
        PulsarClient client,
        PulsarProperties props
    ) throws PulsarClientException
    {
        // Single producer bound to the compacted changelog topic
        return client.newProducer(Schema.STRING)
                     .topic(props.getTopic())
                     .enableBatching(true)
                     .blockIfQueueFull(true)
                     .create();
    }
}
