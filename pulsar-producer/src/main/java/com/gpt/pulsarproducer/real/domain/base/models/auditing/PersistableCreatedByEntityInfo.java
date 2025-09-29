package com.gpt.pulsarproducer.real.domain.base.models.auditing;


public interface PersistableCreatedByEntityInfo
{
    String getEntryUser();

    void setEntryUser(String entryUser);
}
