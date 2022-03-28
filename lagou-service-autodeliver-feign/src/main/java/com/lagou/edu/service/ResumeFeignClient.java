package com.lagou.edu.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @description: 注意
 * 1） @FeignClient注解的name属性⽤于指定要调⽤的服务提供者名称，和服务
 * 提供者yml⽂件中spring.application.name保持⼀致
 * 2）接⼝中的接⼝⽅法，就好⽐是远程服务提供者Controller中的Hander⽅法
 * （只不过如同本地调⽤了），那么在进⾏参数绑定的时，可以使⽤
 * @PathVariable、 @RequestParam、 @RequestHeader等，这也是OpenFeign
 * 对SpringMVC注解的⽀持，但是需要注意value必须设置，否则会抛出异常
 * 使⽤接⼝中⽅法完成远程调⽤（注⼊接⼝即可，实际注⼊的是接⼝的实现）
 * @author: huangguoqiang
 * @create: 2022-03-25 10:55
 **/

@FeignClient(name = "lagou-service-resume", fallback = ResumeFallback.class, path = "/resume")
//@RequestMapping("/resume")
public interface ResumeFeignClient {

    @GetMapping("/openstate/{userId}")
    Integer findDefaultResumeState(@PathVariable Long userId);


}
