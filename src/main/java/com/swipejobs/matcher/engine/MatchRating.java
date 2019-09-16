package com.swipejobs.matcher.engine;

import com.swipejobs.matcher.dto.JobDto;
import com.swipejobs.matcher.dto.WorkerDto;

/**
 * Interface to define some logic that indicates how "well" a given worker
 * matches a given job
 * A rating may represent a requirement (the worker cannot match the job if they
 * do not satisfy the requirement) or a preference (if the worker matches the
 * preference then that match should be recommended over others) or some combination.
 */
public interface MatchRating {

    /**
     * Numeric measure as to how well the worker matches the job
     * Unit of measure depends on implementation
     * If the value is less than 0, then the worker/job pairing does not match
     * some required property and should not be considered acceptable
     * Any value of 0 or greater should be used to calculate the quality of the
     * match. A higher value indicates a higher quality match.
     * Return values from this method are normalized so that values from different
     * implementations may be aggregated.
     *
     * @param worker Worker to calculate match for
     * @param job Job to calculate match for
     * @return Raw value that rates match of worker to job
     */
    double determineRating(WorkerDto worker, JobDto job);
}
