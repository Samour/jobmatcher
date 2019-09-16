package com.swipejobs.matcher.engine;

import com.swipejobs.matcher.dto.JobDto;
import com.swipejobs.matcher.dto.WorkerDto;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class JobPayRateRatingUnitTest {

    private static final double WEIGHT_VALUE = 2;

    private JobPayRateRating jobPayRateRating;

    @Before
    public void constructRating() {
        jobPayRateRating = new JobPayRateRating(WEIGHT_VALUE);
    }

    /**
     * Success case for rating the pay rate
     * Expects the returned value to be proportionate to the job pay rate
     */
    @Test
    public void ratingIsProportionateToPayRateTest() {
        WorkerDto worker = new WorkerDto();
        JobDto job = new JobDto();
        for (int i = 1; i < 10; i++) {
            job.setBillRate(String.format("%s.0", i));
            assertEquals(WEIGHT_VALUE * i, jobPayRateRating.determineRating(worker, job));
        }
    }

    /**
     * Success case for rating the pay rate that contains a dollar symbol
     * Expects the returned value to be proportionate to the job pay rate
     */
    @Test
    public void ratingIsProportionateToPayRateWithDollarSignTest() {
        WorkerDto worker = new WorkerDto();
        JobDto job = new JobDto();
        for (int i = 1; i < 10; i++) {
            job.setBillRate(String.format("$%s.0", i));
            assertEquals(WEIGHT_VALUE * i, jobPayRateRating.determineRating(worker, job));
        }
    }

    /**
     * Error case for pay rate that cannot be parsed
     * Expects 0 to be returned
     */
    @Test
    public void payRateCannotBeParsedTest() {
        WorkerDto worker = new WorkerDto();
        JobDto job = new JobDto();
        job.setBillRate("$a.0");
        assertEquals(0d, jobPayRateRating.determineRating(worker, job));
    }
}
