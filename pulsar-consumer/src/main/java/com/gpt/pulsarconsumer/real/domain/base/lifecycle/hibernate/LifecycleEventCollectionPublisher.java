package com.gpt.pulsarconsumer.real.domain.base.lifecycle.hibernate;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.collection.spi.PersistentCollection;
import org.hibernate.collection.spi.PersistentSet;
import org.hibernate.event.spi.PostCollectionRecreateEvent;
import org.hibernate.event.spi.PostCollectionRecreateEventListener;
import org.hibernate.event.spi.PostCollectionRemoveEvent;
import org.hibernate.event.spi.PostCollectionRemoveEventListener;
import org.hibernate.event.spi.PostCollectionUpdateEvent;
import org.hibernate.event.spi.PostCollectionUpdateEventListener;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Service
@Validated
public class LifecycleEventCollectionPublisher implements PostCollectionUpdateEventListener,
                                                          PostCollectionRecreateEventListener,
                                                          PostCollectionRemoveEventListener
{

    @Override
    public void onPostRecreateCollection(PostCollectionRecreateEvent event)
    {
        log.trace("Received an onPostRecreateCollection event for entity [{}]", event.getAffectedOwnerEntityName());
    }

    @Override
    public void onPostRemoveCollection(PostCollectionRemoveEvent event)
    {
        log.trace("Received an onPostRemoveCollection event for entity [{}]", event.getAffectedOwnerEntityName());
    }

    @Override
    public void onPostUpdateCollection(PostCollectionUpdateEvent event)
    {
        log.trace("Received an onPostUpdateCollection event for entity [{}]", event.getAffectedOwnerEntityName());
        PersistentCollection<?> col = event.getCollection();

        if (col instanceof PersistentSet)
        {
            Set<?> newCollection = new HashSet<>((Set<?>) col.getValue());
            Set<?> oldCollection = new HashSet<>(
                ((HashMap<?, ?>) Objects.requireNonNull(col.getStoredSnapshot())).values());
            Set<?> additions = complement(newCollection, oldCollection);
            Set<?> deletions = complement(oldCollection, newCollection);
        }
    }

    // returns a set that contains all elements of set one that are not in set two without modifying the input
    private static Set<?> complement(final Set<?> setOne, final Set<?> setTwo)
    {
        Set<?> result = new HashSet<>(setOne);
        result.removeIf(setTwo::contains);
        return result;
    }
}
