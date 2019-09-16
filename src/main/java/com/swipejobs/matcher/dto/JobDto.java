package com.swipejobs.matcher.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
public class JobDto {

    private int jobId;
    private String guid;
    private GeographicLocationDto location;
    private String billRate;
    private int workersRequired;
    private boolean driverLicenseRequired;
    private List<String> requiredCertificates;
    private ZonedDateTime startDate;
    private String about;
    private String company;
}
