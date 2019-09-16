package com.swipejobs.matcher.engine;

import com.swipejobs.matcher.dto.JobDto;
import com.swipejobs.matcher.dto.WorkerDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Rating to indicate the rate of pay of the job
 * The higher paying the job the higher the rating, regardless of worker attributes
 */
@Component
public class JobPayRateRating implements MatchRating {

    private final double weightValue;

    public JobPayRateRating(@Value("${engine.rating.jobPayRate.weight}") double weightValue) {
        this.weightValue = weightValue;
    }

    @Override
    public double determineRating(WorkerDto worker, JobDto job) {
        try {
            return weightValue * Double.parseDouble(job.getBillRate().replaceAll("\\$", ""));
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
