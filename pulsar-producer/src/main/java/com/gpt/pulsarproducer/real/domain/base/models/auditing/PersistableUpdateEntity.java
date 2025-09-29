package com.gpt.pulsarproducer.real.domain.base.models.auditing;


import com.gpt.pulsarproducer.real.domain.base.common.GlobalDatabaseConstants;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
@MappedSuperclass
@EntityListeners({ PersistableCreateEntityListener.class, PersistableUpdateEntityListener.class })
public abstract class PersistableUpdateEntity extends PersistableCreateEntity
    implements PersistableCreateEntityInfo, PersistableUpdateEntityInfo
{

    @Column(name = GlobalDatabaseConstants.COL_UPDATE_DATE)
    private LocalDateTime updateDate;
}