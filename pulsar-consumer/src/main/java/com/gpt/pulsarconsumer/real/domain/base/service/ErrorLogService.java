package com.gpt.pulsarconsumer.real.domain.base.service;


import com.gpt.pulsarproducer.real.domain.base.config.ErrorLogEntityConfig;
import com.gpt.pulsarproducer.real.domain.base.models.ErrorLog;
import com.gpt.pulsarproducer.real.domain.base.repository.ErrorLogRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ErrorLogService
{
    private final ErrorLogEntityConfig errorLogEntityConfig;

    private final ErrorLogRepository errorLogRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ErrorLog saveErrorLogRecord(@NotNull ErrorLog errorLog)
    {
        if (!errorLogEntityConfig.isStackTraceEnabled())
        {
            errorLog.setStackTrace(null);
        }
        return errorLogRepository.saveAndFlush(errorLog);
    }
}