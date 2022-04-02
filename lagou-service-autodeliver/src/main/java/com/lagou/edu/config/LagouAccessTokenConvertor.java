package com.lagou.edu.config;

import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @description: 资源服务器取出 JWT 令牌扩展信息
 * 资源服务器也需要⾃定义⼀个转换器类，继承DefaultAccessTokenConverter，重
 * 写extractAuthentication提取⽅法，把载荷信息设置到认证对象的details属性中
 * @author: huangguoqiang
 * @create: 2022-04-02 11:39
 **/
@Component
public class LagouAccessTokenConvertor extends DefaultAccessTokenConverter {
    @Override
    public OAuth2Authentication extractAuthentication(Map<String, ?> map) {
        OAuth2Authentication oAuth2Authentication = super.extractAuthentication(map);
        // 将map放⼊认证对象中，认证对象在controller中可以拿到
        oAuth2Authentication.setDetails(map);
        return oAuth2Authentication;
    }
}
