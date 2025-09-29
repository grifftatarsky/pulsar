package com.gpt.pulsarproducer.real.domain.base.lifecycle.hibernate;


import com.gpt.pulsarproducer.real.domain.base.lifecycle.listener.ChangedProperty;
import com.gpt.pulsarproducer.real.domain.base.lifecycle.listener.LifecycleEvent;
import com.gpt.pulsarproducer.real.domain.base.lifecycle.listener.LifecycleEventType;
import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.event.spi.PostDeleteEvent;
import org.hibernate.event.spi.PostDeleteEventListener;
import org.hibernate.event.spi.PostInsertEvent;
import org.hibernate.event.spi.PostInsertEventListener;
import org.hibernate.event.spi.PostUpdateEvent;
import org.hibernate.event.spi.PostUpdateEventListener;
import org.hibernate.persister.entity.EntityPersister;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LifecycleEventPublisher
    implements PostInsertEventListener, PostUpdateEventListener, PostDeleteEventListener
{

    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void onPostUpdate(PostUpdateEvent event)
    {
        log.trace("Received an update event for entity [{}]", event.getPersister().getEntityName());
        Set<ChangedProperty> properties = new HashSet<>();
        for (int i = 0; i < event.getDirtyProperties().length; i++)
        {
            int dirtyIndex = event.getDirtyProperties()[i];
            String propertyName = event.getPersister().getPropertyNames()[dirtyIndex];
            Object oldValue = event.getOldState()[dirtyIndex];
            Object newValue = event.getState()[dirtyIndex];
            properties.add(new ChangedProperty(propertyName, oldValue, newValue));
            log.debug("Property {} changed from {} to {}", propertyName, oldValue, newValue);
        }

        eventPublisher.publishEvent(new LifecycleEvent(
            LifecycleEventType.UPDATE,
            event.getId(), event.getPersister().getEntityName(),
            getUsername(), properties, copyBean(event.getEntity())
        ));
    }

    @Override
    public void onPostDelete(PostDeleteEvent event)
    {
        eventPublisher.publishEvent(new LifecycleEvent(
            LifecycleEventType.DELETE,
            event.getId(), event.getPersister().getEntityName(),
            getUsername(), null, copyBean(event.getEntity())
        ));
    }

    @Override
    public void onPostInsert(PostInsertEvent event)
    {
        eventPublisher.publishEvent(new LifecycleEvent(
            LifecycleEventType.CREATE,
            event.getId(), event.getPersister().getEntityName(),
            getUsername(), null, copyBean(event.getEntity())
        ));
    }

    private String getUsername()
    {
        return "anonymoususer";
    }

    private Object copyBean(Object source)
    {
        try
        {
            Object target = BeanUtils.instantiateClass(source.getClass());
            BeanUtils.copyProperties(source, target);
            return target;
        }
        catch (Exception e)
        {
            log.warn("Unable to clone bean ", e);
            return source;
        }
    }

    @Override
    public boolean requiresPostCommitHandling(EntityPersister persister)
    {
        return false;
    }
}
