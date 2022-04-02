package com.lagou.edu.pojo;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @description: 实体类开发
 * 实体类统⼀放置到lagou-service-common模块中，包路径为com.lagou.edu.pojo
 * @author: huangguoqiang
 * @create: 2022-03-09 15:21
 **/
@Data
@Entity
@Table(name = "users")
public class Users {
    @Id
    private Long id; // 主键
    private String username; // 用户名
    private String password; //密码

}