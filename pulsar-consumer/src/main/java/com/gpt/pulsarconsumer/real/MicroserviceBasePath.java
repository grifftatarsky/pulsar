package com.gpt.pulsarconsumer.real;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MicroserviceBasePath
{
    TEXT("text"),
    NETCDF("netcdf"),
    CAVE("cave");

    private final String basePath;
}
