package com.swipejobs.matcher.engine;

import com.swipejobs.matcher.dto.GeographicAreaDto;
import com.swipejobs.matcher.dto.GeographicLocationDto;
import com.swipejobs.matcher.dto.JobDto;
import com.swipejobs.matcher.dto.WorkerDto;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

public class JobLocationRatingUnitTest {

    private JobLocationRating jobLocationRating;
    private GeographicDistanceEvaluator geographicDistanceEvaluator;

    @Before
    public void constructRating() {
        geographicDistanceEvaluator = mock(GeographicDistanceEvaluator.class);
        jobLocationRating = new JobLocationRating(1, geographicDistanceEvaluator);
    }

    /**
     * Success case for rating a job that is at the center of the search location
     * Expects 1 to be returned
     */
    @Test
    public void jobIsAtCenterOfSearchTest() {
        GeographicAreaDto areaDto = new GeographicAreaDto();
        areaDto.setMaxJobDistance(3);
        areaDto.setUnit("km");
        WorkerDto worker = new WorkerDto();
        worker.setJobSearchAddress(areaDto);
        JobDto job = new JobDto();
        job.setLocation(new GeographicLocationDto());
        doReturn(0d).when(geographicDistanceEvaluator).determineDistance(areaDto, job.getLocation());
        assertEquals(1d, jobLocationRating.determineRating(worker, job));
    }

    /**
     * Success case for rating a job that is within the search location
     * Expects value between 0 and 1 to be returned
     */
    @Test
    public void jobIsWithinSearchAreaTest() {
        GeographicAreaDto areaDto = new GeographicAreaDto();
        areaDto.setMaxJobDistance(3);
        areaDto.setUnit("km");
        WorkerDto worker = new WorkerDto();
        worker.setJobSearchAddress(areaDto);
        JobDto job = new JobDto();
        job.setLocation(new GeographicLocationDto());
        doReturn(1.5d).when(geographicDistanceEvaluator).determineDistance(areaDto, job.getLocation());
        assertEquals(0.5d, jobLocationRating.determineRating(worker, job));
    }

    /**
     * Success case for rating a job that is on the very edge of the search location
     * Expects 0 to be returned
     */
    @Test
    public void jobIsOnEdgeOfSearchAreaTest() {
        GeographicAreaDto areaDto = new GeographicAreaDto();
        areaDto.setMaxJobDistance(3);
        areaDto.setUnit("km");
        WorkerDto worker = new WorkerDto();
        worker.setJobSearchAddress(areaDto);
        JobDto job = new JobDto();
        job.setLocation(new GeographicLocationDto());
        doReturn(3d).when(geographicDistanceEvaluator).determineDistance(areaDto, job.getLocation());
        assertEquals(0d, jobLocationRating.determineRating(worker, job));
    }

    /**
     * Success case for rating a job that is outside of the search location
     * Expects -1 to be returned
     */
    @Test
    public void jobIsOutsideOfSearchAreaTest() {
        GeographicAreaDto areaDto = new GeographicAreaDto();
        areaDto.setMaxJobDistance(3);
        areaDto.setUnit("km");
        WorkerDto worker = new WorkerDto();
        worker.setJobSearchAddress(areaDto);
        JobDto job = new JobDto();
        job.setLocation(new GeographicLocationDto());
        doReturn(4d).when(geographicDistanceEvaluator).determineDistance(areaDto, job.getLocation());
        assertEquals(-1d, jobLocationRating.determineRating(worker, job));
    }

    /**
     * Error case for rating a worker/job match where the search location cannot
     * be parsed
     * Expects -1 to be returned
     */
    @Test
    public void jobLocationCannotBeParsedTest() {
        GeographicAreaDto areaDto = new GeographicAreaDto();
        areaDto.setMaxJobDistance(3);
        areaDto.setUnit("km");
        WorkerDto worker = new WorkerDto();
        worker.setJobSearchAddress(areaDto);
        JobDto job = new JobDto();
        job.setLocation(new GeographicLocationDto());
        doThrow(new NumberFormatException("Could not parse value"))
                .when(geographicDistanceEvaluator).determineDistance(areaDto, job.getLocation());
        assertEquals(-1d, jobLocationRating.determineRating(worker, job));
    }
}
