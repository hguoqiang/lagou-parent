package com.lagou.edu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @description:
 * @author: huangguoqiang
 * @create: 2022-03-30 21:16
 **/
@SpringBootApplication
@EnableDiscoveryClient
public class LagouCloudOauthApplication {
    public static void main(String[] args) {
        SpringApplication.run(LagouCloudOauthApplication.class, args);
    }
}
