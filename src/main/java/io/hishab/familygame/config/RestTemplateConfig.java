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
    private int internalApiReadTimeout;

    @Value("${special.dice.roll.api.connection-time-out}")
    private int internalApiConnectionTimeout;

    /**
     * Configures a RestTemplate bean with custom timeouts.
     *
     * @param builder the RestTemplateBuilder
     * @return the configured RestTemplate
     */
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                .requestFactory(this::clientHttpRequestFactory)
                .build();
    }

    /**
     * Creates a ClientHttpRequestFactory with custom timeouts.
     *
     * @return the ClientHttpRequestFactory
     */
    private ClientHttpRequestFactory clientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setReadTimeout(internalApiReadTimeout);
        factory.setConnectTimeout(internalApiConnectionTimeout);
        return factory;
    }
}