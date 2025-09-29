package com.gpt.pulsarproducer.service;// package com.gpt.pulsarproducer.service;


import com.gpt.pulsarproducer.real.domain.base.lifecycle.listener.LifecycleEvent;
import com.gpt.pulsarproducer.real.domain.base.models.auditing.PersistableUpdateEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
class LifecycleEventConsumer
{

    private final PulsarService pulsarService;

    @EventListener
    public void handle(LifecycleEvent evt)
    {
        Object copy = evt.getEntity();

        if (copy instanceof PersistableUpdateEntity entity)
        {
            pulsarService.prepareUpsert(evt.getEventType(), entity);
        }
    }
}