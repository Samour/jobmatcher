package com.swipejobs.matcher.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GeographicAreaDto extends GeographicLocationDto {

    private double maxJobDistance;
    private String unit;
}
