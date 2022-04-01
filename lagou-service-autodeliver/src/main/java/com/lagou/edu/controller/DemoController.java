package com.lagou.edu.controller;

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
    public String t1(){
        return "DemoController";
    }
}
