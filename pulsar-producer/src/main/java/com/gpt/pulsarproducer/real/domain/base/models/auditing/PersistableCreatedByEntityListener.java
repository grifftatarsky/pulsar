package com.gpt.pulsarproducer.real.domain.base.models.auditing;


import jakarta.persistence.PrePersist;

public class PersistableCreatedByEntityListener implements PersistableEntityInfoHelper
{
    @PrePersist
    public void createInfo(PersistableCreatedByEntity entity)
    {
        setCreateInfo(entity);
    }
}
