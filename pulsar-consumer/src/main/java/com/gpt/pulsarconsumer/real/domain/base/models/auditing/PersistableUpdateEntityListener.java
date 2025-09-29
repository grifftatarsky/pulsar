package com.gpt.pulsarconsumer.real.domain.base.models.auditing;


import com.gpt.pulsarproducer.real.domain.base.models.auditing.PersistableEntityInfoHelper;
import jakarta.persistence.PreUpdate;

public class PersistableUpdateEntityListener implements PersistableEntityInfoHelper
{
    @PreUpdate
    public void updateInfo(PersistableUpdateEntity entity)
    {
        setUpdateDateTimeInfo(entity);
    }
}
