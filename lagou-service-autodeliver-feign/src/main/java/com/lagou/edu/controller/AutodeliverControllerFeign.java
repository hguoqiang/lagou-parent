package com.lagou.edu.controller;

import com.lagou.edu.service.ResumeFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/autodeliver")
public class AutodeliverControllerFeign {

    @Autowired
    ResumeFeignClient resumeFeignClient;

    @GetMapping("/checkState/{userId}")
    public Integer findResumeOpenState(@PathVariable Long userId) {

        Integer state = resumeFeignClient.findDefaultResumeState(userId);
        System.out.println("======>>>通过feign调⽤简历微服务，获取到⽤户" +
                userId + "的默认简历当前状态为： " + state);
        return state;
    }

}