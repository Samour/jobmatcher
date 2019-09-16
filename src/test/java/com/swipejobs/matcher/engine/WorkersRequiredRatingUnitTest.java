package com.swipejobs.matcher.engine;

import com.swipejobs.matcher.dto.JobDto;
import com.swipejobs.matcher.dto.WorkerDto;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class WorkersRequiredRatingUnitTest {

    private static final double WEIGHT_VALUE = 3;

    private WorkersRequiredRating workersRequiredRating;

    @Before
    public void constructRating() {
        workersRequiredRating = new WorkersRequiredRating(WEIGHT_VALUE);
    }

    /**
     * Success case for evaluating rating for job based on number of workers
     * Expects the returned value to be proportionate to the number of workers
     */
    @Test
    public void ratingIsProportionateToNumberOfWorkersTest() {
        WorkerDto worker = new WorkerDto();
        JobDto job = new JobDto();
        for (int i = 1; i < 8; i++) {
            job.setWorkersRequired(i);
            assertEquals(WEIGHT_VALUE * i, workersRequiredRating.determineRating(worker, job));
        }
    }
}
