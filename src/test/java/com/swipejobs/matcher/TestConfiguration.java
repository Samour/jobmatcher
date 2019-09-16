package com.swipejobs.matcher;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import static org.mockito.Mockito.mock;

@Configuration
public class TestConfiguration {

    /**
     * Use a mock RestTemplate during testing so that we don't hit real endpoints (keep test self-contained)
     *
     * @return Mockito instance of RestTemplate to inject values into
     */
    @Bean
    public RestTemplate swipejobsRestTemplate() {
        return mock(RestTemplate.class);
    }
}
