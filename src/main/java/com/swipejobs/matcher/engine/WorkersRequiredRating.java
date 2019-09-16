package com.swipejobs.matcher.engine;

import com.swipejobs.matcher.dto.JobDto;
import com.swipejobs.matcher.dto.WorkerDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Rating to rank jobs by number of workers
 * A job-seeker is more likely to be successful if there are open positions
 * Therefore give a boost to jobs with more openings
 */
@Component
public class WorkersRequiredRating implements MatchRating {

    private final double weightValue;

    public WorkersRequiredRating(@Value("${engine.rating.workersRequired.weight}") double weightValue) {
        this.weightValue = weightValue;
    }

    @Override
    public double determineRating(WorkerDto worker, JobDto job) {
        return weightValue * job.getWorkersRequired();
    }
}
