package com.gpt.pulsarproducer.real.domain.base.models.auditing;


import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.function.Consumer;
import org.springframework.util.Assert;

public interface PersistableEntityInfoHelper
{

    //default void updateUser(Consumer<? super String> setter)
    //{
    //    setter.accept(GlobalSecurityUtils.getCurrentUserLogin().orElse(GlobalAuthConstants.ANONYMOUS_USER));
    //}

    default void updateTime(Consumer<? super LocalDateTime> setter)
    {
        setter.accept(LocalDateTime.now(ZoneOffset.UTC));
    }

    default void setCreateDateTimeInfo(PersistableCreateEntityInfo entity)
    {
        Assert.notNull(entity, "Null object passed to 'setCreateDateTimeInfo'.");
        // TODO madi add in check for if test profile and
        //  entity entry date is not null so that this can only be done in unit tests
        if (entity.getEntryDate() == null)
        {
            updateTime(entity::setEntryDate);
        }
    }

    default void setUpdateDateTimeInfo(PersistableUpdateEntity entity)
    {
        Assert.notNull(entity, "Null object passed to 'setCreateDateTimeInfo'.");
        updateTime(entity::setUpdateDate);
    }

    default void setCreateInfo(PersistableCreatedByEntity entity)
    {
        Assert.notNull(entity, "Null object passed to 'createInfo'.");
        //updateUser(entity::setEntryUser);
        updateTime(entity::setEntryDate);
    }

    default void setUpdateInfo(PersistableEntity entity)
    {
        Assert.notNull(entity, "Null object passed to 'updateInfo'.");
        //updateUser(entity::setUpdateUser);
        updateTime(entity::setUpdateDate);
    }
}
