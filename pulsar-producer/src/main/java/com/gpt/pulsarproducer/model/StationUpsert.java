package com.gpt.pulsarproducer.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StationUpsert
{
    private String stationId;
    private String type;
    private long version;
    private long updatedAt;
    private boolean deleted;
}
