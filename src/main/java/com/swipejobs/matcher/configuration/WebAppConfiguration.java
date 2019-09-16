package com.swipejobs.matcher.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Configuration
public class WebAppConfiguration {

    /**
     * RestTemplate bean for accessing SwipeJobs REST API
     * Constructs a template that uses the configured endpoint as the API base for requests
     *
     * @param swipejobsEndpoint Base location of API
     * @return RestTemplate configured for SwipeJobs API
     */
    @Bean
    @Profile("!test")
    public RestTemplate swipejobsRestTemplate(@Value("${api.remote.endpoint}") String swipejobsEndpoint) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(swipejobsEndpoint));

        return restTemplate;
    }
}
