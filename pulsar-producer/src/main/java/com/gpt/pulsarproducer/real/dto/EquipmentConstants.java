package com.gpt.pulsarproducer.real.dto;


import java.util.Set;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static com.gpt.pulsarproducer.real.dto.EquipmentType.BUOY_MOBILE;
import static com.gpt.pulsarproducer.real.dto.EquipmentType.SHIP_MOBILE;
import static com.gpt.pulsarproducer.real.dto.EquipmentType.SURFACE_MOBILE;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EquipmentConstants
{
    public static final Set<EquipmentType> MOBILE_EQUIPMENT_TYPES = Set.of(BUOY_MOBILE, SURFACE_MOBILE, SHIP_MOBILE);
}
