package com.gpt.pulsarproducer.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gpt.pulsarproducer.config.PulsarConfig;
import com.gpt.pulsarproducer.model.Upsert;
import com.gpt.pulsarproducer.outbox.OutboxEvent;
import com.gpt.pulsarproducer.outbox.OutboxRepository;
import com.gpt.pulsarproducer.real.domain.Equipment;
import com.gpt.pulsarproducer.real.domain.EquipmentIdentifier;
import com.gpt.pulsarproducer.real.domain.EquipmentTypeIdentifier;
import com.gpt.pulsarproducer.real.domain.base.lifecycle.listener.LifecycleEventType;
import com.gpt.pulsarproducer.real.domain.base.models.ErrorLog;
import com.gpt.pulsarproducer.real.domain.base.models.auditing.PersistableUpdateEntity;
import com.gpt.pulsarproducer.real.domain.base.repository.EquipmentIdentifierRepository;
import com.gpt.pulsarproducer.real.domain.base.repository.EquipmentRepository;
import com.gpt.pulsarproducer.real.domain.base.repository.EquipmentTypeIdentifierRepository;
import com.gpt.pulsarproducer.real.domain.base.service.ErrorLogService;
import com.gpt.pulsarproducer.real.dto.EquipmentDTO;
import com.gpt.pulsarproducer.real.dto.EquipmentIdentifierDTO;
import com.gpt.pulsarproducer.real.dto.EquipmentTypeIdentifierDTO;
import com.gpt.pulsarproducer.real.dto.PersistableUpdateDTO;
import com.gpt.pulsarproducer.real.mappers.EquipmentIdentifierMapper;
import com.gpt.pulsarproducer.real.mappers.EquipmentMapper;
import com.gpt.pulsarproducer.real.mappers.EquipmentTypeIdentifierMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

import static com.gpt.pulsarproducer.real.domain.base.lifecycle.listener.LifecycleEventType.UPDATE;
import static com.gpt.pulsarproducer.service.UpsertObjectType.EQUIPMENT;
import static com.gpt.pulsarproducer.service.UpsertObjectType.EQUIPMENT_IDENTIFIER;
import static com.gpt.pulsarproducer.service.UpsertObjectType.EQUIPMENT_IDENTIFIER_TYPE;

@Slf4j
@Service
@RequiredArgsConstructor
public class PulsarService
{
    // region DI

    private final EquipmentMapper equipmentMapper;
    private final EquipmentIdentifierMapper equipmentIdentifierMapper;
    private final EquipmentTypeIdentifierMapper equipmentTypeIdentifierMapper;

    private final ErrorLogService errorLogService;

    private final ObjectMapper objectMapper;

    private final OutboxRepository outboxRepository;
    private final EquipmentRepository equipmentRepository;
    private final EquipmentIdentifierRepository equipmentIdentifierRepository;
    private final EquipmentTypeIdentifierRepository equipmentTypeIdentifierRepository;

    private final PulsarConfig pulsarConfig;

    // TODO: Will probably need some more robust error handling.

    // endregion

    // region Public
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void prepareUpsert(
        @NotNull LifecycleEventType eventType,
        @NotNull PersistableUpdateEntity entity
    )
    {
        // region Possible to-be-synced objects
        EquipmentDTO equipmentDTO;
        EquipmentIdentifierDTO identifierDTO;
        EquipmentTypeIdentifierDTO typeIdentifierDTO;
        long currentEntityId;
        // endregion

        // region Populate to-be-synced objects
        if (entity instanceof Equipment equipment)
        {
            typeIdentifierDTO = null;
            identifierDTO = null;
            equipmentDTO = equipmentMapper.toDTO(equipment);
            currentEntityId = equipmentDTO.getEquipmentId();
        }
        else
        {
            equipmentDTO = null;

            if (entity instanceof EquipmentIdentifier equipmentIdentifier)
            {
                typeIdentifierDTO = null;
                identifierDTO = equipmentIdentifierMapper.toDTO(equipmentIdentifier);
                currentEntityId = identifierDTO.getEquipmentIdentifierId();
            }
            else
            {
                identifierDTO = null;

                if (entity instanceof EquipmentTypeIdentifier equipmentTypeIdentifier)
                {
                    typeIdentifierDTO = equipmentTypeIdentifierMapper.toDTO(equipmentTypeIdentifier);
                    currentEntityId = typeIdentifierDTO.getIdentifierId();
                }
                else
                {
                    throw new IllegalArgumentException("Unsupported entity type: " + entity.getClass().getName());
                }
            }
        }
        // endregion

        log.debug(
            "notifyServicesOfEntityChanges() -> entityType: {}; entity id: {}; eventType: {}",
            entity.getClass().getSimpleName(),
            currentEntityId,
            eventType
        );

        // region Handle to-be-synced objects
        PersistableUpdateDTO dto =
            equipmentDTO != null ? equipmentDTO : identifierDTO != null ? identifierDTO : typeIdentifierDTO;

        UpsertObjectType type =
            equipmentDTO != null ? EQUIPMENT : identifierDTO != null ? EQUIPMENT_IDENTIFIER : EQUIPMENT_IDENTIFIER_TYPE;

        OutboxEvent event = enqueueUpsert(currentEntityId, dto, type, eventType);
        outboxRepository.saveAndFlush(event);
        // endregion
    }
    // endregion

    // region Internal
    private OutboxEvent enqueueUpsert(
        Long id,
        PersistableUpdateDTO dto,
        UpsertObjectType upsertObjectType,
        LifecycleEventType lifecycleEventType
    )
    {
        boolean deleted = lifecycleEventType.equals(LifecycleEventType.DELETE);

        try
        {
            String dtoJSON = objectMapper.writeValueAsString(dto);

            Upsert up = new Upsert(
                id.toString(),
                dtoJSON,
                // TODO: Implementing a version is required.
                1,
                Instant.now().toEpochMilli(),
                deleted
            );

            String upsertJSON = objectMapper.writeValueAsString(up);

            String topic = switch (upsertObjectType)
            {
                case EQUIPMENT -> pulsarConfig.getEquipmentTopic();
                case EQUIPMENT_IDENTIFIER -> pulsarConfig.getEquipmentIdentifierTopic();
                case EQUIPMENT_IDENTIFIER_TYPE -> pulsarConfig.getEquipmentTypeIdentifierTopic();
            };

            return new OutboxEvent(topic, id.toString(), upsertJSON);
        }
        catch (JsonProcessingException e)
        {
            // TODO: Throw a more appropriate exception for any necessary handling.
            saveError(
                lifecycleEventType,
                upsertObjectType.name(),
                id,
                e.getMessage(),
                Arrays.toString(e.getStackTrace())
            );
            throw new RuntimeException(e);
        }
    }

    private void saveError(
        @NotNull LifecycleEventType eventType,
        @NotBlank String entityType,
        @NotNull Long entityId,
        @NotNull String message,
        @NotNull String stackTrace
    )
    {
        ErrorLog errorLog = new ErrorLog(
            null,
            eventType + ":" + entityType + ":" + entityId,
            null,
            null,
            this.getClass().getName(),
            message,
            stackTrace,
            null
        );
        errorLogService.saveErrorLogRecord(errorLog);
    }
    // endregion

    // region Snapshot
    @Transactional
    @EventListener(ApplicationReadyEvent.class)
    protected void runEquipmentTypeIdentifierSnapshot()
    {
        StopWatch typeIDWatch = new StopWatch();
        typeIDWatch.start();
        // CHOICE: This is not ideal. I'd like to refactor this snapshot so it takes place in SQL.
        PageRequest pageRequest = PageRequest.of(0, 5_000, Sort.by("identifierId"));

        Page<EquipmentTypeIdentifier> page;

        do
        {
            page = equipmentTypeIdentifierRepository.findAll(pageRequest);

            var batch = new ArrayList<OutboxEvent>(page.getNumberOfElements());

            for (var entity : page.getContent())
            {
                EquipmentTypeIdentifierDTO dto = equipmentTypeIdentifierMapper.toDTO(entity);
                batch.add(enqueueUpsert(entity.getIdentifierId(), dto, EQUIPMENT_IDENTIFIER_TYPE, UPDATE));
            }
            outboxRepository.saveAll(batch);
            pageRequest = pageRequest.next();
        }
        while (page.hasNext());
        typeIDWatch.stop();
        log.info("typeIDWatch time: {} ms", typeIDWatch.getTotalTimeMillis());
    }

    @Transactional
    @EventListener(ApplicationReadyEvent.class)
    protected void runEquipmentIdentifierSnapshot()
    {
        StopWatch equipIDWatch = new StopWatch();
        equipIDWatch.start();
        // CHOICE: This is not ideal. I'd like to refactor this snapshot so it takes place in SQL.
        PageRequest pageRequest = PageRequest.of(0, 5_000, Sort.by("equipmentIdentifierId"));

        Page<EquipmentIdentifier> page;

        do
        {
            page = equipmentIdentifierRepository.findAll(pageRequest);

            var batch = new ArrayList<OutboxEvent>(page.getNumberOfElements());

            for (var entity : page.getContent())
            {
                EquipmentIdentifierDTO dto = equipmentIdentifierMapper.toDTO(entity);
                batch.add(enqueueUpsert(entity.getEquipmentIdentifierId(), dto, EQUIPMENT_IDENTIFIER, UPDATE));
            }
            outboxRepository.saveAll(batch);
            pageRequest = pageRequest.next();
        }

        while (page.hasNext());
        equipIDWatch.stop();
        log.info("equipIDWatch time: {} ms", equipIDWatch.getTotalTimeMillis());
    }

    @Transactional
    @EventListener(ApplicationReadyEvent.class)
    protected void runEquipmentSnapshot()
    {
        StopWatch equipWatch = new StopWatch();
        equipWatch.start();
        // CHOICE: This is not ideal. I'd like to refactor this snapshot so it takes place in SQL.
        PageRequest pageRequest = PageRequest.of(0, 10_000, Sort.by("equipmentId"));

        Page<Equipment> page;

        do
        {
            page = equipmentRepository.findAll(pageRequest);

            var batch = new ArrayList<OutboxEvent>(page.getNumberOfElements());

            for (var entity : page.getContent())
            {
                EquipmentDTO dto = equipmentMapper.toDTO(entity);
                batch.add(enqueueUpsert(entity.getEquipmentId(), dto, EQUIPMENT, UPDATE));
            }
            outboxRepository.saveAll(batch);
            pageRequest = pageRequest.next();
        }
        while (page.hasNext());
        equipWatch.stop();
        log.info("equipWatch time: {} ms", equipWatch.getTotalTimeMillis());
    }
    // endregion
}
