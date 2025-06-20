package com.gharelu.auth_service.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.netflix.eureka.EurekaDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
@Configuration
public class WebClientConfig {

    @Autowired
    EurekaDiscoveryClient discoveryClient;

    @Bean
    @Scope(value = "prototype")
    public WebClient customerServiceWebClientEurekaDiscovered(WebClient.Builder webClientBuilder) {
        List<ServiceInstance> instances = discoveryClient.getInstances("customer-service");

        if(instances.isEmpty()){
            throw new RuntimeException("No instances found for customer-service");
        }

        String hostname = instances.get(0).getHost();
        String port = String.valueOf(instances.get(0).getPort());

        return webClientBuilder
                .baseUrl(String.format("http://%s:%s/customer", hostname, port))
                .build();
    }
}

