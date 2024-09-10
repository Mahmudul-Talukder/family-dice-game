package io.hishab.familygame.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;


@Configuration
public class RestTemplateConfig {
    @Value("${special.dice.roll.api.read-time-out}")
    int internalApiReadTimeout;

    @Value("${special.dice.roll.api.connection-time-out}")
    int internalApiConnectionTimeout;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                .requestFactory(this::clientHttpRequestFactory)
                .build();
    }

    /**
     * Client http request factory client http request factory.
     *
     * @return the client http request factory
     */
    private ClientHttpRequestFactory clientHttpRequestFactory() {
        SimpleClientHttpRequestFactory simpleClientHttpRequestFactory = new SimpleClientHttpRequestFactory();
        simpleClientHttpRequestFactory.setReadTimeout(internalApiReadTimeout);
        simpleClientHttpRequestFactory.setConnectTimeout(internalApiConnectionTimeout);
        return simpleClientHttpRequestFactory;
    }
}