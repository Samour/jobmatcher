package com.swipejobs.matcher.engine;

import com.swipejobs.matcher.dto.GeographicLocationDto;

/**
 * Interface to determine the distance between two points
 */
public interface GeographicDistanceEvaluator {

    /**
     * Calculate the distance (in km) between two points
     *
     * @param location1 The first point
     * @param location2 The second point
     * @return The distance (in km) between the two points
     */
    double determineDistance(GeographicLocationDto location1, GeographicLocationDto location2);
}
