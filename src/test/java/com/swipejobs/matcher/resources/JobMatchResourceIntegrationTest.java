package com.swipejobs.matcher.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swipejobs.matcher.dto.JobDto;
import com.swipejobs.matcher.dto.WorkerDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class JobMatchResourceIntegrationTest {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private RestTemplate swipejobsRestTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @Before
    public void constructMockMvc() throws IOException {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        // Return API responses from disk
        when(swipejobsRestTemplate.exchange(
                eq("/workers"),
                eq(HttpMethod.GET),
                any(),
                eq(new ParameterizedTypeReference<List<WorkerDto>>() {
                })
        )).thenReturn(new ResponseEntity<>(
                (List<WorkerDto>) objectMapper.readValue(
                        getClass().getClassLoader().getResourceAsStream("workers.json"),
                        objectMapper.getTypeFactory().constructCollectionType(List.class, WorkerDto.class)
                ),
                HttpStatus.OK
        ));
        when(swipejobsRestTemplate.exchange(
                eq("/jobs"),
                eq(HttpMethod.GET),
                any(),
                eq(new ParameterizedTypeReference<List<JobDto>>() {
                })
        )).thenReturn(new ResponseEntity<>(
                (List<JobDto>) objectMapper.readValue(
                        getClass().getClassLoader().getResourceAsStream("jobs.json"),
                        objectMapper.getTypeFactory().constructCollectionType(List.class, JobDto.class)
                ),
                HttpStatus.OK
        ));
    }

    /**
     * Success case for retrieving jobs for user 0
     * Simple smoke test to ensure expected values are returned
     */
    @Test
    public void runForUser0Test() throws Exception {
        mockMvc.perform(
                get("/api/findJobsForWorker")
                        .param("workerId", "0")
        ).andExpect(jsonPath("$.[*].jobId").value(contains(37, 26, 35)))
                .andExpect(jsonPath("$.[*].billRate").value(contains("$12.22", "$19.79", "$10.24")));
    }

    /**
     * Success case for retrieving jobs for user 1
     * Simple smoke test to ensure expected values are returned
     */
    @Test
    public void runForUser1Test() throws Exception {
        mockMvc.perform(
                get("/api/findJobsForWorker")
                        .param("workerId", "1")
        ).andExpect(jsonPath("$.[*].jobId").value(contains(38, 12, 34)))
                .andExpect(jsonPath("$.[*].billRate").value(contains("$15.32", "$16.19", "$14.85")));
    }
}
