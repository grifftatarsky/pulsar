package com.gpt.pulsarproducer.real.mappers;


import com.gpt.pulsarproducer.real.domain.Equipment;
import com.gpt.pulsarproducer.real.domain.EquipmentIdentifier;
import com.gpt.pulsarproducer.real.domain.Station;
import com.gpt.pulsarproducer.real.dto.EquipmentDTO;
import com.gpt.pulsarproducer.real.dto.EquipmentIdentifierDTO;
import com.gpt.pulsarproducer.real.dto.SimpleEquipmentDTO;
import com.gpt.pulsarproducer.real.dto.SimpleEquipmentIdentifierDTO;
import com.gpt.pulsarproducer.real.dto.SimpleStationDTO;
import com.gpt.pulsarproducer.real.dto.StationDTO;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;

/**
 * Helper class containing common mapping logic shared across multiple mappers. This eliminates code duplication and
 * provides consistent mapping behavior.
 */
@Component
@Validated
@RequiredArgsConstructor
public class BaseMapperHelper
{

    private final EquipmentTypeIdentifierMapper equipmentTypeIdentifierMapper;

    /**
     * Maps a Station entity to a SimpleStationDTO (minimal version to prevent circular references).
     *
     * @param entity
     *     The Station entity to map
     *
     * @return The mapped SimpleStationDTO or null if entity is null
     */
    public SimpleStationDTO toSimpleStationDTO(Station entity)
    {
        if (entity == null)
        {
            return null;
        }

        SimpleStationDTO dto = new SimpleStationDTO();
        dto.setStationId(entity.getStationId());
        dto.setCallSign(entity.getCallSign());
        dto.setName(entity.getName());
        dto.setCity(entity.getCity());
        dto.setState(entity.getState());
        dto.setCountry(entity.getCountry());
        dto.setWmoIndex(entity.getWmoIndex());
        dto.setWmoRegion(entity.getWmoRegion());
        dto.setEndDate(entity.getEndDate());
        dto.setValidated(entity.isValidated());
        return dto;
    }

    /**
     * Maps a Station entity to a StationDTO with full equipment information.
     *
     * @param entity
     *     The Station entity to map
     *
     * @return The mapped StationDTO or null if entity is null
     */
    public StationDTO toStationDTOWithEquipments(Station entity)
    {
        if (entity == null)
        {
            return null;
        }

        StationDTO dto = new StationDTO(
            entity.getStationId(),
            entity.getCallSign(),
            entity.getName(),
            entity.getCity(),
            entity.getState(),
            entity.getCountry(),
            entity.getWmoIndex(),
            entity.getWmoRegion(),
            entity.getEndDate(),
            entity.isValidated(),
            new ArrayList<>()
        );

        List<SimpleEquipmentDTO> equipmentDTOs = CollectionUtils.isEmpty(entity.getEquipments())
                                                 ? new ArrayList<>()
                                                 : entity.getEquipments()
                                                         .stream()
                                                         .map(this::toSimpleEquipmentDTO)
                                                         .collect(Collectors.toList());

        dto.setEquipments(equipmentDTOs);
        return dto;
    }

    /**
     * Maps a StationDTO to a Station entity.
     *
     * @param dto
     *     The StationDTO to map
     *
     * @return The mapped Station entity or null if dto is null
     */
    public Station toStationEntity(StationDTO dto)
    {
        if (dto == null)
        {
            return null;
        }

        Station entity = new Station(
            dto.getStationId(),
            dto.getCallSign(),
            dto.getName(),
            dto.getCity(),
            dto.getState(),
            dto.getCountry(),
            dto.getWmoIndex(),
            dto.getWmoRegion(),
            dto.getEndDate(),
            new ArrayList<>(),
            dto.isValidated()
        );

        List<Equipment> equipments = CollectionUtils.isEmpty(dto.getEquipments())
                                     ? new ArrayList<>()
                                     : dto.getEquipments()
                                          .stream()
                                          .map(x -> toEquipmentEntityFromSimple(x, entity))
                                          .collect(Collectors.toList());

        entity.setEquipments(equipments);
        return entity;
    }

    /**
     * Maps an Equipment entity to a SimpleEquipmentDTO (minimal version to prevent circular references).
     *
     * @param entity
     *     The Equipment entity to map
     *
     * @return The mapped SimpleEquipmentDTO or null if entity is null
     */
    public SimpleEquipmentDTO toSimpleEquipmentDTO(Equipment entity)
    {
        if (entity == null)
        {
            return null;
        }
        SimpleStationDTO stationDTO = entity.getStation() != null ? toSimpleStationDTO(entity.getStation()) : null;

        SimpleEquipmentDTO dto = new SimpleEquipmentDTO(
            entity.getEquipmentId(),
            stationDTO,
            entity.getEquipmentType(),
            entity.getLocation(),
            entity.getStartDateTime(),
            entity.getEndDateTime(),
            entity.isValidated(),
            null
        );

        List<SimpleEquipmentIdentifierDTO> identifiers =
            CollectionUtils.isEmpty(entity.getIdentifiers())
            ? new ArrayList<>()
            : entity.getIdentifiers()
                    .stream()
                    .map(this::toSimpleEquipmentIdentifierDTO)
                    .collect(Collectors.toList());

        dto.setIdentifiers(identifiers);
        return dto;
    }

    /**
     * Maps an Equipment entity to an EquipmentDTO with full station information.
     *
     * @param entity
     *     The Equipment entity to map
     *
     * @return The mapped EquipmentDTO or null if entity is null
     */
    public EquipmentDTO toEquipmentDTOWithStation(Equipment entity)
    {
        if (entity == null)
        {
            return null;
        }

        SimpleStationDTO stationDTO = entity.getStation() != null ? toSimpleStationDTO(entity.getStation()) : null;

        EquipmentDTO dto = new EquipmentDTO(
            entity.getEquipmentId(),
            stationDTO,
            entity.getEquipmentType(),
            entity.getLocation(),
            entity.getStartDateTime(),
            entity.getEndDateTime(),
            entity.isValidated(),
            new ArrayList<>()
        );

        List<SimpleEquipmentIdentifierDTO> identifiers =
            CollectionUtils.isEmpty(entity.getIdentifiers())
            ? new ArrayList<>()
            : entity.getIdentifiers()
                    .stream()
                    .map(this::toSimpleEquipmentIdentifierDTO)
                    .collect(Collectors.toList());

        dto.setIdentifiers(identifiers);
        return dto;
    }

    /**
     * Maps an EquipmentDTO to an Equipment entity.
     *
     * @param dto
     *     The EquipmentDTO to map
     * @param station
     *     The parent Station entity
     *
     * @return The mapped Equipment entity or null if dto is null
     */
    public Equipment toEquipmentEntity(EquipmentDTO dto, Station station)
    {
        if (dto == null)
        {
            return null;
        }

        Equipment entity = new Equipment(
            dto.getEquipmentId(),
            station,
            dto.getEquipmentType(),
            dto.getLocation(),
            dto.getStartDateTime(),
            dto.getEndDateTime(),
            dto.isValidated(),
            new ArrayList<>()
        );

        List<EquipmentIdentifier> identifiers =
            CollectionUtils.isEmpty(dto.getIdentifiers())
            ? new ArrayList<>()
            : dto.getIdentifiers()
                 .stream()
                 .map(x -> toEquipmentIdentifierEntityFromSimple(x, entity))
                 .collect(Collectors.toList());

        entity.setIdentifiers(identifiers);
        return entity;
    }

    /**
     * Maps a SimpleEquipmentDTO to an Equipment entity.
     *
     * @param dto
     *     The SimpleEquipmentDTO to map
     * @param station
     *     The parent Station entity
     *
     * @return The mapped Equipment entity or null if dto is null
     */
    public Equipment toEquipmentEntityFromSimple(SimpleEquipmentDTO dto, Station station)
    {
        if (dto == null)
        {
            return null;
        }

        Equipment entity = new Equipment(
            dto.getEquipmentId(),
            station,
            dto.getEquipmentType(),
            dto.getLocation(),
            dto.getStartDateTime(),
            dto.getEndDateTime(),
            dto.isValidated(),
            new ArrayList<>()
        );

        List<EquipmentIdentifier> identifiers =
            CollectionUtils.isEmpty(dto.getIdentifiers())
            ? new ArrayList<>()
            : dto.getIdentifiers()
                 .stream()
                 .map(x -> toEquipmentIdentifierEntityFromSimple(x, entity))
                 .collect(Collectors.toList());

        entity.setIdentifiers(identifiers);
        return entity;
    }

    /**
     * Maps an EquipmentIdentifier entity to a SimpleEquipmentIdentifierDTO.
     *
     * @param entity
     *     The EquipmentIdentifier entity to map
     *
     * @return The mapped SimpleEquipmentIdentifierDTO or null if entity is null
     */
    public SimpleEquipmentIdentifierDTO toSimpleEquipmentIdentifierDTO(EquipmentIdentifier entity)
    {
        if (entity == null)
        {
            return null;
        }

        SimpleEquipmentIdentifierDTO dto = new SimpleEquipmentIdentifierDTO();
        dto.setEquipmentIdentifierId(entity.getEquipmentIdentifierId());
        dto.setEquipmentId(entity.getEquipment() != null ? entity.getEquipment().getEquipmentId() : null);
        dto.setTypeIdentifier(equipmentTypeIdentifierMapper.toDTO(entity.getTypeIdentifier()));
        dto.setValue(entity.getValue());
        return dto;
    }

    /**
     * Maps an EquipmentIdentifier entity to an EquipmentIdentifierDTO.
     *
     * @param entity
     *     The EquipmentIdentifier entity to map
     * @param equipmentDTO
     *     The parent SimpleEquipmentDTO
     *
     * @return The mapped EquipmentIdentifierDTO or null if entity is null
     */
    public EquipmentIdentifierDTO toEquipmentIdentifierDTO(
        EquipmentIdentifier entity, @NotNull SimpleEquipmentDTO equipmentDTO)
    {
        if (entity == null)
        {
            return null;
        }

        return new EquipmentIdentifierDTO(
            entity.getEquipmentIdentifierId(),
            equipmentDTO,
            equipmentTypeIdentifierMapper.toDTO(entity.getTypeIdentifier()),
            entity.getValue()
        );
    }

    /**
     * Maps a SimpleEquipmentIdentifierDTO to an EquipmentIdentifier entity.
     *
     * @param dto
     *     The SimpleEquipmentIdentifierDTO to map
     * @param equipment
     *     The parent Equipment entity
     *
     * @return The mapped EquipmentIdentifier entity or null if dto is null
     */
    public EquipmentIdentifier toEquipmentIdentifierEntityFromSimple(
        SimpleEquipmentIdentifierDTO dto,
        @NotNull Equipment equipment
    )
    {
        if (dto == null)
        {
            return null;
        }

        return new EquipmentIdentifier(
            dto.getEquipmentIdentifierId(),
            equipment,
            equipmentTypeIdentifierMapper.toEntity(dto.getTypeIdentifier()),
            dto.getValue()
        );
    }

    /**
     * Maps an EquipmentIdentifierDTO to an EquipmentIdentifier entity.
     *
     * @param dto
     *     The EquipmentIdentifierDTO to map
     * @param equipment
     *     The parent Equipment entity
     *
     * @return The mapped EquipmentIdentifier entity or null if dto is null
     */
    public EquipmentIdentifier toEquipmentIdentifierEntity(EquipmentIdentifierDTO dto, @NotNull Equipment equipment)
    {
        if (dto == null)
        {
            return null;
        }

        return new EquipmentIdentifier(
            dto.getEquipmentIdentifierId(),
            equipment,
            equipmentTypeIdentifierMapper.toEntity(dto.getTypeIdentifier()),
            dto.getValue()
        );
    }
}