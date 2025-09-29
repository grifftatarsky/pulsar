package com.gpt.pulsarconsumer.real.domain.base.models.auditing;


import com.gpt.pulsarproducer.real.domain.base.common.GlobalDatabaseConstants;
import com.gpt.pulsarproducer.real.domain.base.models.auditing.PersistableCreateEntity;
import com.gpt.pulsarproducer.real.domain.base.models.auditing.PersistableCreateEntityListener;
import com.gpt.pulsarproducer.real.domain.base.models.auditing.PersistableCreatedByEntityInfo;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@MappedSuperclass
@EntityListeners({ PersistableCreateEntityListener.class })
public abstract class PersistableCreatedByEntity extends PersistableCreateEntity
    implements PersistableCreatedByEntityInfo
{
    @Column(
        name = GlobalDatabaseConstants.COL_ENTRY_USER,
        length = GlobalDatabaseConstants.ENTRY_UPDATE_USER_LENGTH,
        updatable = false
    )
    @Size(max = GlobalDatabaseConstants.ENTRY_UPDATE_USER_LENGTH)
    private String entryUser;
}
