package com.lagou.edu.service;

import com.lagou.edu.pojo.Resume;

public interface ResumeService {
    /**
     * 根据用户id查询用户默认的公开简历
     * @param userId
     * @return
     */
    Resume findDefaultResumeByUserId(Long userId);
}