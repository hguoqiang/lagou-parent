package com.lagou.edu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableDiscoveryClient
//@EnableHystrix //开启Hystrix功能
@EnableCircuitBreaker //开启断路器 ，通用的注解
public class AutodeliverApplication {
    public static void main(String[] args) {
        SpringApplication.run(AutodeliverApplication.class, args);
    }

    /**
     * 注⼊RestTemplate
     *
     * @return
     */
    @LoadBalanced
    @Bean
    public RestTemplate getRestTemplate() {

        return new RestTemplate();
    }
}