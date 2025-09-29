package com.gpt.pulsarproducer.real.domain.base.repository;


import com.gpt.pulsarproducer.real.domain.base.models.ErrorLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ErrorLogRepository
    extends JpaRepository<ErrorLog, Long>, JpaSpecificationExecutor<ErrorLog>,
            PagingAndSortingRepository<ErrorLog, Long>
{

}
