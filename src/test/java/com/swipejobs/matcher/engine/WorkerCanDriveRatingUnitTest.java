package com.swipejobs.matcher.engine;

import com.swipejobs.matcher.dto.JobDto;
import com.swipejobs.matcher.dto.WorkerDto;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class WorkerCanDriveRatingUnitTest {

    private WorkerCanDriveRating workerCanDriveRating;

    @Before
    public void constructRating() {
        workerCanDriveRating = new WorkerCanDriveRating();
    }

    /**
     * Success case for evaluating whether a worker that cannot drive may
     * work at a job where driving is required
     * Expects -1 to be returned
     */
    @Test
    public void workerCannotDriveButIsRequiredTest() {
        WorkerDto worker = new WorkerDto();
        worker.setHasDriversLicense(false);
        JobDto job = new JobDto();
        job.setDriverLicenseRequired(true);
        assertEquals(-1d, workerCanDriveRating.determineRating(worker, job));
    }

    /**
     * Success case for evaluating whether a worker that can drive may
     * work at a job where driving is required
     * Expects 0 to be returned
     */
    @Test
    public void workerCanDriveIsRequiredTest() {
        WorkerDto worker = new WorkerDto();
        worker.setHasDriversLicense(true);
        JobDto job = new JobDto();
        job.setDriverLicenseRequired(true);
        assertEquals(0d, workerCanDriveRating.determineRating(worker, job));
    }

    /**
     * Success case for evaluating whether a worker that cannot drive may
     * work at a job where driving is not required
     * Expects 0 to be returned
     */
    @Test
    public void workerCannotDriveNotRequiredTest() {
        WorkerDto worker = new WorkerDto();
        worker.setHasDriversLicense(false);
        JobDto job = new JobDto();
        job.setDriverLicenseRequired(false);
        assertEquals(0d, workerCanDriveRating.determineRating(worker, job));
    }
}
