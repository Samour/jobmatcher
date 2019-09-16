package com.swipejobs.matcher.services;

import com.swipejobs.matcher.dto.JobDto;
import com.swipejobs.matcher.dto.WorkerDto;
import com.swipejobs.matcher.engine.MatchRating;
import com.swipejobs.matcher.exceptions.ResourceNotFoundException;
import com.swipejobs.matcher.repositories.RestRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JobMatchServiceImpl implements JobMatchService {

    private static final String MSG_BAD_WORKER_ID = "Could not find worker with id '%s'";

    @AllArgsConstructor
    @Getter
    private static class JobScoreTuple {
        private JobDto jobDto;
        private double rating;
    }

    private final List<MatchRating> matchRatings;
    private final RestRepository restRepository;

    public JobMatchServiceImpl(List<MatchRating> matchRatings, RestRepository restRepository) {
        this.matchRatings = Collections.unmodifiableList(matchRatings);
        this.restRepository = restRepository;
    }

    @Override
    public List<JobDto> findBestJobsForWorker(int workerId, int jobLimit) {
        Optional<WorkerDto> workerOptional = restRepository.findWorkerById(workerId);
        if (!workerOptional.isPresent()) {
            throw new ResourceNotFoundException(String.format(MSG_BAD_WORKER_ID, workerId));
        }

        WorkerDto worker = workerOptional.get();

        return restRepository.findAllJobs().stream()
                .map(j -> {
                    JobScoreTuple tuple = new JobScoreTuple(j, 0);
                    for (MatchRating matchRating : matchRatings) {
                        double rating = matchRating.determineRating(worker, j);
                        if (rating < 0) {
                            return null;
                        } else {
                            tuple.rating += rating;
                        }
                    }
                    return tuple;
                }).filter(Objects::nonNull)
                .sorted((a, b) -> Double.compare(b.getRating(), a.getRating())) // Sort highest first
                .map(JobScoreTuple::getJobDto)
                .limit(jobLimit)
                .collect(Collectors.toList());
    }
}
