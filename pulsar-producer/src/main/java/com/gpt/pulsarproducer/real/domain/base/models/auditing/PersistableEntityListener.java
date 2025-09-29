package com.gpt.pulsarproducer.real.domain.base.models.auditing;


import jakarta.persistence.PreUpdate;

public class PersistableEntityListener implements PersistableEntityInfoHelper
{
    @PreUpdate
    public void updateInfo(PersistableEntity entity)
    {
        setUpdateInfo(entity);
    }
}
