package com.swipejobs.matcher.engine;

import com.swipejobs.matcher.dto.JobDto;
import com.swipejobs.matcher.dto.WorkerDto;
import org.springframework.stereotype.Component;

/**
 * Rating to determine if worker is required to drive for job
 * If the job requires the worker to be able to drive and they cannot,
 * -1 will be returned
 * Otherwise 0 is returned
 */
@Component
public class WorkerCanDriveRating implements MatchRating {

    @Override
    public double determineRating(WorkerDto worker, JobDto job) {
        if (job.isDriverLicenseRequired() && !worker.isHasDriversLicense()) {
            return -1;
        } else {
            return 0;
        }
    }
}
