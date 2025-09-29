package com.gpt.pulsarconsumer.real.domain.base.models.auditing;


import java.time.LocalDateTime;

public interface PersistableCreateEntityInfo
{

    LocalDateTime getEntryDate();

    void setEntryDate(LocalDateTime entryDate);
}
