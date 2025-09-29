package com.gpt.pulsarconsumer.real.dto;


public enum EquipmentType
{
    AIRCRAFT_WAYPOINT,
    AIRCRAFT_PIREP,
    BUOY_MOBILE,
    BUOY_FIXED,
    CMAN,
    GOES_R,
    HYDRO,
    /** general use case */
    ICAO,
    /**
     * Base category type for US mesonet stations. Subnets may be defined in the range 1000-1999 by adding this value to
     * the Mesonet id. Example Iowa DOT, IADOT(91 so the subnet id(1091))
     */
    MESONET,
    MESONET_NWS_FAA,
    NEXRAD,
    /** Profiler site locations */
    PROFILER,
    /** Legacy SAO identifiers - Still some around */
    SAO,
    /** I believe these are ships that don't want to tell us who they are??? */
    SHIP_MOBILE,
    STRANGER_STATION,
    SURFACE_MOBILE,
    SURFACE_FIXED,
    WFO_ID,
    UPPER_AIR
}
