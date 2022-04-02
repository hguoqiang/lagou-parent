package com.lagou.edu.config;

import com.lagou.edu.service.JdbcUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @description: 该配置类，主要处理用户名和密码的校验
 * 客户端 传到 username 和 password 到认证服务器
 * 一般来说，username和password都存在认证服务器的数据库中用户表中
 * 根据username查询用户表，验证当前传过来的用户信息的合法性
 * @author: huangguoqiang
 * @create: 2022-03-31 14:17
 **/
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    /**
     * 注册一个认证管理器对象到容器
     *
     * @return
     * @throws Exception
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    /**
     * 密码编码器
     *
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    /**
     * 这个方法 主要处理用户名和密码的校验
     * 1、客户端 传到 username 和 password 到认证服务器
     * 2、一般来说，username和password都存在认证服务器的数据库中用户表中
     * 3、根据username查询用户表，验证当前传过来的用户信息的合法性
     *
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //在这个方法中可以关联数据库查询用户信息了，当前我们先把用户信息配置在内存中

        /*//实例化一个用户对象,相当于数据库用户表中一条记录
        UserDetails userDetails = new User("admin", "123456", new ArrayList<>());

        //把用户记录加到 oauth2 的环境当中，当有用户名和密码传入过来后，会进行自动匹配，匹配不上就不合法
        auth.inMemoryAuthentication()
                .withUser(userDetails)
                .passwordEncoder(passwordEncoder());*/


        //从数据库查询用户信息
        auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());


    }

    @Override
    @Bean
    protected UserDetailsService userDetailsService() {

        return new JdbcUserDetailsService();
    }
}
