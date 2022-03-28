package com.lagou.edu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients // 开启Feign
public class AutodeliverApplicationFeign {
    public static void main(String[] args) {
        SpringApplication.run(AutodeliverApplicationFeign.class, args);
    }


}