package com.allblue.seller.infrastructure.cafe24;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class Cafe24RestClientConfig {

    @Bean
    public RestClient restClient() {
        return RestClient.create();
    }
}
