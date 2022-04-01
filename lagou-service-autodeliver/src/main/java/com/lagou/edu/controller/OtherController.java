package com.lagou.edu.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @author: huangguoqiang
 * @create: 2022-03-31 21:36
 **/
@RestController
@RequestMapping("/other")
public class OtherController {

    @RequestMapping("/ttt")
    public String ttt(){
        return "OtherController";
    }
}
