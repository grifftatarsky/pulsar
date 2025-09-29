package com.gpt.pulsarconsumer.real.domain.base.models.auditing;


import java.time.LocalDateTime;

public interface PersistableEntityInfo
{

    String getUpdateUser();

    void setUpdateUser(String updateUser);

    LocalDateTime getUpdateDate();

    void setUpdateDate(LocalDateTime updateDate);
}
