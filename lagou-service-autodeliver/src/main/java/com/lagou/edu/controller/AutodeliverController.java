package com.lagou.edu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/autodeliver")
public class AutodeliverController {
    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/checkState/{userId}")
    public Integer findResumeOpenState(@PathVariable Long userId) {
        Integer forObject =
                restTemplate.getForObject("http://localhost:8080/resume/openstate/"
                        + userId, Integer.class);
        System.out.println("======>>>调⽤简历微服务，获取到⽤户" +
                userId + "的默认简历当前状态为： " + forObject);
        return forObject;
    }
    /*
    我们在⾃动投递微服务中使⽤RestTemplate调⽤简历微服务的简历状态接⼝时
（Restful API 接⼝）。在微服务分布式集群环境下会存在什么问题呢？怎么解决？
存在的问题：
1）在服务消费者中，我们把url地址硬编码到代码中，不⽅便后期维护。
2）服务提供者只有⼀个服务，即便服务提供者形成集群，服务消费者还需要⾃⼰实
现负载均衡。
3）在服务消费者中，不清楚服务提供者的状态。
4）服务消费者调⽤服务提供者时候，如果出现故障能否及时发现不向⽤户抛出异常⻚⾯？
5） RestTemplate这种请求调⽤⽅式是否还有优化空间？能不能类似于Dubbo那样
玩？
6）这么多的微服务统⼀认证如何实现？
7）配置⽂件每次都修改好多个很麻烦！？
8） ....
上述分析出的问题，其实就是微服务架构中必然⾯临的⼀些问题：
1）服务管理：⾃动注册与发现、状态监管
2）服务负载均衡
3）熔断
4）远程过程调⽤
5）⽹关拦截、路由转发
6）统⼀认证
7）集中式配置管理，配置信息实时⾃动更新
这些问题， Spring Cloud 体系都有解决⽅案，后续我们会逐个学习。
     */


    /*
    改造：服务消费者调⽤服务提供者（通过Eureka）
     */
    //注入服务发现客户端
    @Autowired
    private DiscoveryClient discoveryClient;

    @GetMapping("/checkState2/{userId}")
    public Integer findResumeOpenState2(@PathVariable Long userId) {
        //从注册中心拿到服务，进行访问
        //获取eureka中注册的服务列表
        List<ServiceInstance> instances = discoveryClient.getInstances("lagou-service-resume");
        //获取实例，此处不考虑负载均衡，就拿第一个
        ServiceInstance serviceInstance = instances.get(0);
        //从元数据信息中获取相关host,port根据实例信息拼接请求地址
        String host = serviceInstance.getHost();
        int port = serviceInstance.getPort();
        URI uri = serviceInstance.getUri();

        String url = "http://" + host + ":" + port + "/resume/openstate/" + userId;

        //消费者直接调用提供者
        Integer forObject =
                restTemplate.getForObject(url, Integer.class);
        System.out.println("======>>>通过Eureka集群获取服务实例，调⽤简历微服务，获取到⽤户" +
                userId + "的默认简历当前状态为： " + forObject);
        return forObject;
    }
}