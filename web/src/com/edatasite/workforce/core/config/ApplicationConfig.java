package com.edatasite.workforce.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Configuration
public class ApplicationConfig {

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate(new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()));
        restTemplate.setInterceptors(Collections.singletonList(new LoggingRequestInterceptor()));
        return restTemplate;
    }

    @Bean
    public RestTemplate hmrcRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();

        List<ClientHttpRequestInterceptor> interceptors = restTemplate.getInterceptors();

        interceptors.add((request, body, execution) -> {
            request.getHeaders().add("Accept", "application/vnd.hmrc.1.0+json");
            return execution.execute(request, body);
        });
        interceptors.add(new LoggingRequestInterceptor());
        restTemplate.setInterceptors(interceptors);
        return restTemplate;
    }
}
