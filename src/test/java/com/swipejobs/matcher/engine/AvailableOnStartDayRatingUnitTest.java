package com.swipejobs.matcher.engine;

import com.swipejobs.matcher.dto.DayDto;
import com.swipejobs.matcher.dto.JobDto;
import com.swipejobs.matcher.dto.WorkerDto;
import org.junit.Before;
import org.junit.Test;

import java.time.ZonedDateTime;
import java.util.ArrayList;

import static junit.framework.TestCase.assertEquals;

public class AvailableOnStartDayRatingUnitTest {

    private static final double RATING_WEIGHT = 1;

    private AvailableOnStartDayRating availableOnStartDayRating;

    @Before
    public void constructRating() {
        availableOnStartDayRating = new AvailableOnStartDayRating(RATING_WEIGHT);
    }

    /**
     * Success case for worker being available on the start day
     * Expects RATING_WEIGHT to be returned
     */
    @Test
    public void isAvailableOnStartDayTest() {
        WorkerDto worker = new WorkerDto();
        worker.setAvailability(new ArrayList<>());
        DayDto dayDto = new DayDto();
        dayDto.setDayIndex(1);
        dayDto.setTitle("Monday");
        worker.getAvailability().add(dayDto);
        JobDto job = new JobDto();
        job.setStartDate(ZonedDateTime.parse("2019-09-16T09:03:08.063Z"));
        assertEquals(RATING_WEIGHT, availableOnStartDayRating.determineRating(worker, job));
    }

    /**
     * Success case for worker not being available on the start day
     * Expects 0 to be returned
     */
    @Test
    public void isntAvailableOnStartDayTest() {
        WorkerDto worker = new WorkerDto();
        worker.setAvailability(new ArrayList<>());
        DayDto dayDto = new DayDto();
        dayDto.setDayIndex(2);
        dayDto.setTitle("Tuesday");
        worker.getAvailability().add(dayDto);
        JobDto job = new JobDto();
        job.setStartDate(ZonedDateTime.parse("2019-09-16T09:03:08.063Z"));
        assertEquals(0d, availableOnStartDayRating.determineRating(worker, job));
    }

    /**
     * Success case for evaluating worker availability with a null element in the availability array
     * Expects method to complete successfully without NPE
     */
    @Test
    public void availabilityHasNullEntryTest() {
        WorkerDto worker = new WorkerDto();
        worker.setAvailability(new ArrayList<>());
        worker.getAvailability().add(null);
        DayDto dayDto = new DayDto();
        dayDto.setDayIndex(1);
        dayDto.setTitle("Monday");
        worker.getAvailability().add(dayDto);
        JobDto job = new JobDto();
        job.setStartDate(ZonedDateTime.parse("2019-09-16T09:03:08.063Z"));
        assertEquals(RATING_WEIGHT, availableOnStartDayRating.determineRating(worker, job));
    }
}
