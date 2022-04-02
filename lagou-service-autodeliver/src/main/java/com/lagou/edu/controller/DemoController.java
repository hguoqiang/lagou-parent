package com.lagou.edu.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @author: huangguoqiang
 * @create: 2022-03-31 21:36
 **/
@RestController
@RequestMapping("/demo")
public class DemoController {

    @RequestMapping("/t1")
    public String t1() {


        //业务类⽐如Controller类中，可以通过SecurityContextHolder.getContext().getAuthentication()获取到认证对象，进⼀步获取到扩展信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object details = authentication.getDetails();
        System.out.println(details);
        return "DemoController";
    }
}
