package com.lagou.edu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
// 声明本项⽬是⼀个Eureka服务
@EnableEurekaServer
public class LagouCloudEurekaServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(LagouCloudEurekaServerApplication.class, args);
    }
}