package com.gpt.pulsarconsumer.real.domain.base.models.auditing;


import com.gpt.pulsarproducer.real.domain.base.models.auditing.PersistableEntityInfoHelper;
import jakarta.persistence.PrePersist;

public class PersistableCreatedByEntityListener implements PersistableEntityInfoHelper
{
    @PrePersist
    public void createInfo(PersistableCreatedByEntity entity)
    {
        setCreateInfo(entity);
    }
}
