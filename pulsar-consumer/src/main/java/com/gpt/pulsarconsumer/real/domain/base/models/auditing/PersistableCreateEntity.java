package com.gpt.pulsarconsumer.real.domain.base.models.auditing;


import com.gpt.pulsarproducer.real.domain.base.common.GlobalDatabaseConstants;
import com.gpt.pulsarproducer.real.domain.base.models.auditing.PersistableCreateEntityInfo;
import com.gpt.pulsarproducer.real.domain.base.models.auditing.PersistableCreateEntityListener;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@MappedSuperclass
@EntityListeners({ PersistableCreateEntityListener.class })
public class PersistableCreateEntity implements PersistableCreateEntityInfo
{

    @Column(name = GlobalDatabaseConstants.COL_ENTRY_DATE, updatable = false)
    private LocalDateTime entryDate;
}