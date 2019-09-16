package com.swipejobs.matcher.engine;

import com.swipejobs.matcher.dto.GeographicLocationDto;
import org.springframework.stereotype.Component;

/**
 * Very simple implementation of calculating the difference between two points
 * This will being to break down at larger scales due to the curvature of the earth,
 * but it gives a good enough approximation at small scales
 */
@Component
public class PythagorasDistanceEvaluator implements GeographicDistanceEvaluator {

    // Rough values and conversion technique taken from
    // https://stackoverflow.com/questions/1253499/simple-calculations-for-working-with-lat-lon-and-km-distance
    private static final double LAT_TO_KM = 110.574;
    private static final double LONG_TO_KM_C = 11.320;

    private double latToKm(double lat) {
        return LAT_TO_KM * lat;
    }

    private double longToKm(double lat, double lng) {
        return LONG_TO_KM_C * Math.cos(Math.toRadians(lat)) * lng;
    }

    @Override
    public double determineDistance(GeographicLocationDto location1, GeographicLocationDto location2) {
        double latDiff = latToKm(Double.parseDouble(location1.getLatitude()) - Double.parseDouble(location2.getLatitude()));
        // I think this may still have some issues re: modding over 360 deg.
        double longDiff = longToKm(
                Math.abs(Double.parseDouble(location1.getLatitude()) - Double.parseDouble(location2.getLatitude())) / 2,
                Double.parseDouble(location1.getLongitude()) - Double.parseDouble(location2.getLongitude())
        );

        return Math.sqrt(latDiff * latDiff + longDiff * longDiff);
    }
}
