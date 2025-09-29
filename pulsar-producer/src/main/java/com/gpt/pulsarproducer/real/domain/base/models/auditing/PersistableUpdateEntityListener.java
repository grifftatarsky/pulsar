package com.gpt.pulsarproducer.real.domain.base.models.auditing;


import jakarta.persistence.PreUpdate;

public class PersistableUpdateEntityListener implements PersistableEntityInfoHelper
{
    @PreUpdate
    public void updateInfo(PersistableUpdateEntity entity)
    {
        setUpdateDateTimeInfo(entity);
    }
}
