package com.swipejobs.matcher.services;

import com.swipejobs.matcher.dto.JobDto;

import java.util.List;

/**
 * Service to facilitate matching between workers and jobs
 */
public interface JobMatchService {

    /**
     * Find the top jobs that match a given worker
     * Jobs will only be returned if the worker satisfies the compulsory requirements
     * of the job
     * Jobs are ordered in descending order by how good a match they are for the worker
     * If more jobs match than jobLimit, only the first jobLimit jobs are returned
     *
     * @param workerId ID of worker to find matches for
     * @param jobLimit Limit on number of jobs to return
     * @return Top matching jobs for worker
     */
    List<JobDto> findBestJobsForWorker(int workerId, int jobLimit);
}
