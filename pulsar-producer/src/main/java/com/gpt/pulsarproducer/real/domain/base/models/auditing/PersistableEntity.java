package com.gpt.pulsarproducer.real.domain.base.models.auditing;


import com.gpt.pulsarproducer.real.domain.base.common.GlobalDatabaseConstants;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@MappedSuperclass
@EntityListeners({ PersistableCreatedByEntityListener.class, PersistableEntityListener.class })
public abstract class PersistableEntity extends PersistableCreatedByEntity
    implements PersistableCreatedByEntityInfo, PersistableEntityInfo
{
    @Column(name = GlobalDatabaseConstants.COL_UPDATE_USER, length = GlobalDatabaseConstants.ENTRY_UPDATE_USER_LENGTH)
    @Size(max = GlobalDatabaseConstants.ENTRY_UPDATE_USER_LENGTH)
    private String updateUser;

    @Column(name = GlobalDatabaseConstants.COL_UPDATE_DATE)
    private LocalDateTime updateDate;
}