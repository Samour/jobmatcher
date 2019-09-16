package com.swipejobs.matcher.repositories;

import com.swipejobs.matcher.dto.JobDto;
import com.swipejobs.matcher.dto.WorkerDto;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Repository
public class RestRepositoryImpl implements RestRepository {

    private static final String ENDPOINT_WORKERS = "/workers";
    private static final String ENDPOINT_JOBS = "/jobs";

    private final RestTemplate restTemplate;

    public RestRepositoryImpl(RestTemplate swipejobsRestTemplate) {
        restTemplate = swipejobsRestTemplate;
    }

    @Override
    public List<WorkerDto> findAllWorkers() {
        return restTemplate.exchange(
                ENDPOINT_WORKERS,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<WorkerDto>>() {
                }
        ).getBody();
    }

    @Override
    public Optional<WorkerDto> findWorkerById(int workerId) {
        return findAllWorkers().stream()
                .filter(w -> w.getUserId() == workerId)
                .findFirst();
    }

    @Override
    public List<JobDto> findAllJobs() {
        return restTemplate.exchange(
                ENDPOINT_JOBS,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<JobDto>>() {
                }
        ).getBody();
    }
}
