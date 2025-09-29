package com.gpt.pulsarconsumer.real.dto;


import com.gpt.pulsarproducer.real.dto.EquipmentType;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;
import org.springframework.util.CollectionUtils;

import static com.gpt.pulsarproducer.real.dto.EquipmentConstants.MOBILE_EQUIPMENT_TYPES;

/**
 * A common validation strategy interface for equipment validation that can be used by both DTOs and entity validators.
 */
public interface EquipmentValidationStrategy
{

    /**
     * Validates if mobile equipment has a null location.
     *
     * @param equipmentType
     *     the type of equipment
     * @param location
     *     the location of the equipment
     *
     * @return true if validation passes, false otherwise
     */
    default boolean validateMobileEquipmentLocation(@NotNull EquipmentType equipmentType, Object location)
    {
        if (MOBILE_EQUIPMENT_TYPES.contains(equipmentType))
        {
            return location == null;
        }
        // DO NOT try to validate the stationary equipment has a location
        // we do not want to do this b/c we create stations on the fly in the decoder
        // and do NOT get locations for stationary equipment in files
        return true;
    }

    /**
     * Validates equipment type specific rules.
     *
     * @param messages
     *     list to collect validation messages
     * @param equipmentType
     *     the type of equipment
     * @param location
     *     the location
     */
    default void validateEquipmentTypeRules(
        @NotNull List<String> messages,
        @NotNull EquipmentType equipmentType,
        Object location
    )
    {
        if (!validateMobileEquipmentLocation(equipmentType, location))
        {
            messages.add("Mobile equipments must have a null location.");
        }
    }

    /**
     * Validates station identifiers.
     *
     * @param messages
     *     list to collect validation messages
     * @param stationId
     *     the station ID
     * @param callSign
     *     the call sign
     */
    default void validateStationIdentifiers(
        @NotNull List<String> messages,
        Long stationId,
        String callSign
    )
    {
        if (stationId == null && callSign == null)
        {
            messages.add("stationId or callSign is required.");
        }
    }

    /**
     * Validates there are no duplicate equipmentTypes in the list (it's a set, so it shouldn't happen anyway).
     *
     * @param messages
     *     list to collect validation messages
     * @param equipmentTypes
     *     the list of equipmentTypes
     */
    default void validateNoDuplicateEquipmentTypes(
        @NotNull List<String> messages, @NotNull Set<EquipmentType> equipmentTypes)
    {
        if (!CollectionUtils.isEmpty(equipmentTypes)
            && equipmentTypes.size()
            != equipmentTypes.stream().distinct().toList().size())
        {
            messages.add("EquipmentTypeIdentifier.EquipmentTypes cannot contain duplicates.");
        }
    }
}