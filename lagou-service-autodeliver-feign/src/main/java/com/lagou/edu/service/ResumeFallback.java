package com.lagou.edu.service;

import org.springframework.stereotype.Component;

/**
 * @description: 服务降级 处理类，降级回退逻辑需要定义⼀个类，实现FeignClient接⼝，实现接⼝中的⽅法
 * @author: huangguoqiang
 * @create: 2022-03-25 12:01
 **/
@Component
public class ResumeFallback implements ResumeFeignClient {
    @Override
    public Integer findDefaultResumeState(Long userId) {
        return -1;
    }
}
