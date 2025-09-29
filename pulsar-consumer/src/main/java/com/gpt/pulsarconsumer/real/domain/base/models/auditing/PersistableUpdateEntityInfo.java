package com.gpt.pulsarconsumer.real.domain.base.models.auditing;


import java.time.LocalDateTime;

public interface PersistableUpdateEntityInfo
{
    LocalDateTime getUpdateDate();

    void setUpdateDate(LocalDateTime updateDate);
}
