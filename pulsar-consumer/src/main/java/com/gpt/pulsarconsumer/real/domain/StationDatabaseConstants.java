package com.gpt.pulsarconsumer.real.domain;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StationDatabaseConstants
{
    // tables
    public static final String TBL_STATION = "stations";
    public static final String TBL_EQUIPMENT = "equipment";
    public static final String TBL_EQUIPMENT_TYPE_IDENTIFIERS = "equipment_type_identifiers";
    public static final String TBL_EQUIPMENT_IDENTIFIERS = "equipment_identifiers";
    public static final String
        TBL_EQUIPMENT_TYPE_IDENTIFIERS_EQUIPMENT_TYPE
        = "equipment_type_identifiers_equipment_types";

    // columns
    public static final String COL_STATION_ID = "station_id";
    public static final String COL_EQUIPMENT_ID = "equipment_id";
    public static final String COL_STATION_CALL_SIGN = "call_sign";
    public static final String COL_EQUIPMENT_TYPE = "equipment_type";

    public static final String COL_EQUIPMENT_IDENTIFIER_ID = "equipment_identifier_id";
    public static final String COL_IDENTIFIER_ID = "identifier_id";
    public static final String COL_IDENTIFIER = "identifier";
}

