package com.swipejobs.matcher.repositories;

import com.swipejobs.matcher.dto.JobDto;
import com.swipejobs.matcher.dto.WorkerDto;

import java.util.List;
import java.util.Optional;

/**
 * Repository to retrieve data from REST API
 */
public interface RestRepository {

    /**
     * @return List of all existing workers
     */
    List<WorkerDto> findAllWorkers();

    /**
     * Retrieve a worker by ID
     *
     * @param workerId ID of worker to retrieve
     * @return Optional containing worker details, if exists
     */
    Optional<WorkerDto> findWorkerById(int workerId);

    /**
     * @return List of all existing jobs
     */
    List<JobDto> findAllJobs();
}
