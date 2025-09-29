package com.gpt.pulsarconsumer.real.domain.base.models;


import com.gpt.pulsarproducer.real.domain.base.models.auditing.PersistableCreateEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import static com.gpt.pulsarproducer.real.domain.base.common.GlobalDatabaseConstants.COL_ERROR_LOG_ID;
import static com.gpt.pulsarproducer.real.domain.base.common.GlobalDatabaseConstants.TBL_ERROR_LOG;

@Getter
@Setter
@Entity
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = TBL_ERROR_LOG)
@NamedQuery(name = "ErrorLog.findAll", query = "SELECT el FROM ErrorLog el")
public class ErrorLog extends PersistableCreateEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = COL_ERROR_LOG_ID, nullable = false, insertable = false, updatable = false)
    private Long errorLogId;

    @Column
    private String key;

    @Column
    private String dataType;

    @Column
    private String bucket;

    @Column
    private String errorLocation;

    @Column
    private String exception;

    @Column
    private String message;

    @Column
    private String stackTrace;
}
