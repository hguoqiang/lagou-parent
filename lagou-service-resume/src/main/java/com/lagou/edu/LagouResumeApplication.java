package com.lagou.edu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EntityScan("com.lagou.edu.pojo")
/**
 * @EnableEurekaClient  开启 eureka client ，eureka 独有的注解
 * @EnableDiscoveryClient 开启注册中心客户端 ，通用性的注解，比如 nacos 等
 * 从 spring cloud  的 Edgware 版本开始，不加上面两个注解也可以，但还是建议加上，见名知意
 */
@EnableEurekaClient
@EnableDiscoveryClient
public class LagouResumeApplication {
    public static void main(String[] args) {
        SpringApplication.run(LagouResumeApplication.class, args);
    }
}