package com.gpt.pulsarproducer.real.domain.base.models.auditing;


import jakarta.persistence.PrePersist;

public class PersistableCreateEntityListener implements PersistableEntityInfoHelper
{
    @PrePersist
    public void createInfo(PersistableCreateEntity entity)
    {
        setCreateDateTimeInfo(entity);
    }
}
