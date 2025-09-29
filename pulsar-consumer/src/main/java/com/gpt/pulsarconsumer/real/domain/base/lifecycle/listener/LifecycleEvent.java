package com.gpt.pulsarconsumer.real.domain.base.lifecycle.listener;


import com.gpt.pulsarproducer.real.domain.base.lifecycle.listener.ChangedProperty;
import com.gpt.pulsarproducer.real.domain.base.lifecycle.listener.LifecycleEventType;
import jakarta.validation.constraints.NotNull;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@RequiredArgsConstructor
@AllArgsConstructor
public class LifecycleEvent
{
    @NotNull
    private LifecycleEventType eventType;

    // TODO why the warning without the transient??? .... what am i missing
    @NotNull
    private Object id;

    @NotNull
    private String entityType;

    private String modifier;

    private Set<ChangedProperty> properties;

    // TODO why the warning without the transient??? .... what am i missing
    private Object entity;
}
