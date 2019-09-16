package com.swipejobs.matcher.engine;

import com.swipejobs.matcher.dto.GeographicLocationDto;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class PythagorasDistanceEvaluatorUnitTest {

    private PythagorasDistanceEvaluator pythagorasDistanceEvaluator;

    @Before
    public void constructEvaluator() {
        pythagorasDistanceEvaluator = new PythagorasDistanceEvaluator();
    }

    /**
     * Simple test case for pythagoras calculation
     * Expects correct value to be returned
     */
    @Test
    public void simplePythagCalcTest() {
        GeographicLocationDto location1 = new GeographicLocationDto();
        location1.setLatitude("0.01");
        location1.setLongitude("0.035");
        GeographicLocationDto location2 = new GeographicLocationDto();
        location2.setLatitude("0.068");
        location2.setLongitude("0.042");
        assertEquals(6.413781509628733, pythagorasDistanceEvaluator.determineDistance(location1, location2));
    }

    /**
     * Simple test case for pythagoras calculation with the same latitudinal value
     * Expects correct value to be returned
     */
    @Test
    public void latD0Test() {
        GeographicLocationDto location1 = new GeographicLocationDto();
        location1.setLatitude("0.01");
        location1.setLongitude("0.035");
        GeographicLocationDto location2 = new GeographicLocationDto();
        location2.setLatitude("0.01");
        location2.setLongitude("0.042");
        // Got some underflow, but it's not too important to worry about
        assertEquals(0.07923999999999999, pythagorasDistanceEvaluator.determineDistance(location1, location2));
    }

    /**
     * Simple test case for pythagoras calculation with the same longitudinal value
     * Expects correct value to be returned
     */
    @Test
    public void longD0Test() {
        GeographicLocationDto location1 = new GeographicLocationDto();
        location1.setLatitude("0.01");
        location1.setLongitude("0.035");
        GeographicLocationDto location2 = new GeographicLocationDto();
        location2.setLatitude("0.068");
        location2.setLongitude("0.035");
        assertEquals(6.413292, pythagorasDistanceEvaluator.determineDistance(location1, location2));
    }
}
