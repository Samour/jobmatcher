package com.swipejobs.matcher.engine;

import com.swipejobs.matcher.dto.JobDto;
import com.swipejobs.matcher.dto.WorkerDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Rating to indicate convenience of job location
 * If job is outside of worker's search area, -1 will be returned
 * Otherwise the returned value will be proportionate to how close the job is
 * to the worker's center of search
 */
@Component
public class JobLocationRating implements MatchRating {

    private final double weightValue;
    private final GeographicDistanceEvaluator geographicDistanceEvaluator;

    public JobLocationRating(@Value("${engine.rating.jobLocation.weight}") double weightValue,
                             GeographicDistanceEvaluator geographicDistanceEvaluator) {
        this.weightValue = weightValue;
        this.geographicDistanceEvaluator = geographicDistanceEvaluator;
    }

    @Override
    public double determineRating(WorkerDto worker, JobDto job) {
        if (!"km".equals(worker.getJobSearchAddress().getUnit())) {
            // Currently only supporting KM
            // TODO we should probably log this occurrence
            throw new IllegalArgumentException("Currently cannot evaluate units other than KM");
        }

        double distance;
        try {
            distance = geographicDistanceEvaluator.determineDistance(worker.getJobSearchAddress(), job.getLocation());
        } catch (NumberFormatException e) {
            // TODO log the exception
            return -1; // Rather than return error to caller, just assume the job is outside of the search area
        }

        if (distance > worker.getJobSearchAddress().getMaxJobDistance()) {
            return -1;
        } else {
            return weightValue * (worker.getJobSearchAddress().getMaxJobDistance() - distance) / worker.getJobSearchAddress().getMaxJobDistance();
        }
    }
}
