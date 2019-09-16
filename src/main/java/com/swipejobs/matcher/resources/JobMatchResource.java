package com.swipejobs.matcher.resources;

import com.swipejobs.matcher.dto.JobDto;
import com.swipejobs.matcher.services.JobMatchService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class JobMatchResource {

    private final int jobsToReturn;
    private final JobMatchService jobMatchService;

    public JobMatchResource(@Value("${api.jobsToReturn}") int jobsToReturn, JobMatchService jobMatchService) {
        this.jobsToReturn = jobsToReturn;
        this.jobMatchService = jobMatchService;
    }

    /**
     * Find the top jobs for a given worker
     *
     * @param workerId ID of worker to find jobs for
     * @return List of top matching jobs
     */
    @RequestMapping(value = "/api/findJobsForWorker", method = RequestMethod.GET)
    public ResponseEntity<List<JobDto>> findBestJobs(@RequestParam int workerId) {
        return new ResponseEntity<>(jobMatchService.findBestJobsForWorker(workerId, jobsToReturn), HttpStatus.OK);
    }
}
