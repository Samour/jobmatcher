package com.swipejobs.matcher.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class WorkerDto {

    private String guid;
    private int userId;
    @JsonProperty("isActive")
    private boolean active;
    private String phone;
    private String email;
    private NameDto name;
    private int age;
    private int rating;
    private List<String> certificates;
    private List<String> skills;
    private GeographicAreaDto jobSearchAddress;
    private String transportation;
    private boolean hasDriversLicense;
    List<DayDto> availability;
}
