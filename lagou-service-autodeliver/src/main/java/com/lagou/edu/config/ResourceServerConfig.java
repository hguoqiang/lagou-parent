package com.lagou.edu.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;

/**
 * @description:
 * @author: huangguoqiang
 * @create: 2022-03-31 20:36
 **/
@Configuration
@EnableResourceServer//开启资源服务器功能
@EnableWebSecurity //开启web访问安全功能,访问接口的安全
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {


    /**
     * 该方法用于资源服务器向远程服务器发起请求，进行token校验。/oauth/check_token
     *
     * @param resources
     * @throws Exception
     */
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {

        //设置当前资源服务器的id
        resources.resourceId("autodeliver");

        //定义token服务对象
        RemoteTokenServices remoteTokenServices = new RemoteTokenServices();
        //设置校验端点接口
        remoteTokenServices.setCheckTokenEndpointUrl("http://localhost:9999/oauth/check_token");
        //携带客户端 client_id 和 密码 client_secret
        remoteTokenServices.setClientId("client_test");
        remoteTokenServices.setClientSecret("test123");

        resources.tokenServices(remoteTokenServices);

    }


    /**
     * 场景：一个服务器中有很多资源（API接口）
     * 有的API接口必须通过认证后才能访问
     * 有的API接口压根不需要认证，本来就是对外开放的接口
     * 我们需要对不同的接口进行区分对待，设置是否需要经过认证
     *
     * @param http
     * @throws Exception
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        //session创建策略设置为根据需要时候才创建
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);

        http.authorizeRequests()
                .antMatchers("/autodeliver/**").authenticated()//autodeliver 为前缀的请求需要认证
                .antMatchers("/demo/**").authenticated()// demo 为前缀的请求需要认证
                .anyRequest().permitAll();// 放行

    }


}
