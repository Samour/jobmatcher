package com.swipejobs.matcher.services;

import com.swipejobs.matcher.dto.JobDto;
import com.swipejobs.matcher.dto.WorkerDto;
import com.swipejobs.matcher.engine.MatchRating;
import com.swipejobs.matcher.exceptions.ResourceNotFoundException;
import com.swipejobs.matcher.repositories.RestRepository;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JobMatchServiceImplUnitTest {

    private RestRepository restRepository;
    private MatchRating matchRating1;
    private MatchRating matchRating2;
    private MatchRating matchRating3;
    private JobMatchService jobMatchService;

    @Before
    public void constructService() {
        matchRating1 = mock(MatchRating.class);
        matchRating2 = mock(MatchRating.class);
        matchRating3 = mock(MatchRating.class);
        restRepository = mock(RestRepository.class);
        jobMatchService = new JobMatchServiceImpl(
                Arrays.asList(matchRating1, matchRating2, matchRating3),
                restRepository
        );
    }

    /**
     * Success case for retrieving jobs in order of rank
     * Expects jobs to be returned in expected order
     */
    @Test
    public void jobsAreOrderedByRatingTest() {
        WorkerDto worker = new WorkerDto();
        List<JobDto> jobs = IntStream.range(0, 5)
                .mapToObj(i -> new JobDto())
                .collect(Collectors.toList());
        when(restRepository.findWorkerById(1)).thenReturn(Optional.of(worker));
        when(restRepository.findAllJobs()).thenReturn(jobs);
        // Set up rating results so that we expect list to be returned in reverse, except with jobs.get(1)
        // at the front
        for (int i = 0; i < jobs.size(); i++) {
            doReturn(1d).when(matchRating1).determineRating(worker, jobs.get(i));
            doReturn((double) i).when(matchRating2).determineRating(worker, jobs.get(i));
            doReturn((double) (i + 3)).when(matchRating3).determineRating(worker, jobs.get(i));
        }
        doReturn(5000d).when(matchRating3).determineRating(worker, jobs.get(1));

        assertThat(jobMatchService.findBestJobsForWorker(1, 100), contains(
                jobs.get(1),
                jobs.get(4),
                jobs.get(3),
                jobs.get(2),
                jobs.get(0)
        ));
    }

    /**
     * Success case for retrieving only the top N jobs
     * Expects only 3 jobs to be returned in expected order
     */
    @Test
    public void onlyTopNJobsAreReturnedTest() {
        WorkerDto worker = new WorkerDto();
        List<JobDto> jobs = IntStream.range(0, 5)
                .mapToObj(i -> new JobDto())
                .collect(Collectors.toList());
        when(restRepository.findWorkerById(1)).thenReturn(Optional.of(worker));
        when(restRepository.findAllJobs()).thenReturn(jobs);
        // Set up rating results so that we expect list to be returned in reverse, except with jobs.get(1)
        // at the front
        for (int i = 0; i < jobs.size(); i++) {
            doReturn(1d).when(matchRating1).determineRating(worker, jobs.get(i));
            doReturn((double) i).when(matchRating2).determineRating(worker, jobs.get(i));
            doReturn((double) (i + 3)).when(matchRating3).determineRating(worker, jobs.get(i));
        }
        doReturn(5000d).when(matchRating3).determineRating(worker, jobs.get(1));

        assertThat(jobMatchService.findBestJobsForWorker(1, 3), contains(
                jobs.get(1),
                jobs.get(4),
                jobs.get(3)
        ));
    }

    /**
     * Success case for retrieving jobs with a worker not meeting the requirements
     * of some jobs
     * Expects only jobs to be returned that match the requirements
     */
    @Test
    public void jobsWithMissingRequirementsAreExcludedTest() {
        WorkerDto worker = new WorkerDto();
        List<JobDto> jobs = IntStream.range(0, 5)
                .mapToObj(i -> new JobDto())
                .collect(Collectors.toList());
        when(restRepository.findWorkerById(1)).thenReturn(Optional.of(worker));
        when(restRepository.findAllJobs()).thenReturn(jobs);
        // Set up rating results so that we expect list to be returned in reverse, except with jobs.get(1)
        // at the front
        for (int i = 0; i < jobs.size(); i++) {
            doReturn(1d).when(matchRating1).determineRating(worker, jobs.get(i));
            doReturn((double) i).when(matchRating2).determineRating(worker, jobs.get(i));
            doReturn((double) (i + 3)).when(matchRating3).determineRating(worker, jobs.get(i));
        }
        doReturn(5000d).when(matchRating3).determineRating(worker, jobs.get(1));
        // Worker does not match requirements for jobs 0 and 3
        doReturn(-1d).when(matchRating2).determineRating(worker, jobs.get(0));
        doReturn(-3d).when(matchRating1).determineRating(worker, jobs.get(3));

        assertThat(jobMatchService.findBestJobsForWorker(1, 100), contains(
                jobs.get(1),
                jobs.get(4),
                jobs.get(2)
        ));
    }

    /**
     * Error case for retrieving jobs for a worker that does not exist
     * Expects ResourceNotFoundException to be raised
     */
    @Test
    public void workerNotPresentThrowsExceptionTest() {
        when(restRepository.findWorkerById(1)).thenReturn(Optional.empty());
        ResourceNotFoundException ex = assertThrows(
                ResourceNotFoundException.class,
                () -> jobMatchService.findBestJobsForWorker(1, 100)
        );
        assertEquals("Could not find worker with id '1'", ex.getMessage());
    }
}
