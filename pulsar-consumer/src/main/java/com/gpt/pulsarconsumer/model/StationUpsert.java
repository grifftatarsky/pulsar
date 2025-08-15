package com.gpt.pulsarconsumer.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StationUpsert
{
    private String stationId;
    private String type;      // "A" | "B" | "C"
    private long version;
    private long updatedAt;   // epoch millis
    private boolean deleted;  // true => delete
}
