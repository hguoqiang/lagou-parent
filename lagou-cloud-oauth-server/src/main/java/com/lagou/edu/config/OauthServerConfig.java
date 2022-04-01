package com.lagou.edu.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

/**
 * @description: 当前类为Oauth2 server的配置类（需要继承特定的⽗类AuthorizationServerConfigurerAdapter）
 * @author: huangguoqiang
 * @create: 2022-03-30 21:35
 **/
@Configuration
@EnableAuthorizationServer//开启认证服务器功能
public class OauthServerConfig extends AuthorizationServerConfigurerAdapter {
    @Autowired
    private AuthenticationManager authenticationManager;



    /**
     * 认证服务安全配置，接口权限等。⽤来配置令牌端点的安全约束。
     * 认证服务器最终是以api接⼝的⽅式对外提供服务（校验合法性并⽣成令牌、校验令牌等）
     * 那么，以api接⼝⽅式对外的话，就涉及到接⼝的访问权限，我们需要在这⾥进⾏必要的配置
     *
     * @param security
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        super.configure(security);

        //相当于打开endpoint 访问接口的开关，这样后期可以访问接口
        //设置对3个方法的操作
        security.allowFormAuthenticationForClients()//允许客户端表单认证
                .tokenKeyAccess("permitAll()")//开启端口（/oauth/token_key）访问权限，允许
                .checkTokenAccess("permitAll()")//开启端口（/oauth/check_token）访问权限，允许
        ;

    }

    /**
     * 客户端详情相关的配置，我作为一个认证服务器，我需要知道哪些客户端可以向我认证。
     * ⽐如client_id， secret
     * 当前这个服务就如同QQ平台，拉勾⽹作为客户端需要qq平台进⾏登录授权认证等，提前需要到QQ平台注册， QQ平台会给拉勾⽹颁发client_id等必要参数，表明客户端是谁
     * ⽤来配置客户端详情服务（ClientDetailsService），客户端详情信息在 这⾥进⾏初始化，你能够把客户端详情信息写死在这⾥ 或者 是通过数据库来存储调取详情信息
     *
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        super.configure(clients);
        clients.inMemory()//客户端信息存储在什么地方，可以是内存、数据库等
                .withClient("client_test")//添加一个client配置，指定client_id
                .secret("test123")//指定客户端的密码
                .resourceIds("autodeliver")//指定客户端所能访问的资源id清单，此处的资源id也需要在资源服务器配置一样的
                //认证类型、令牌颁发模式，可以配置多个，但不一定都能用到，具体使用哪种方式，需要客户端调用时候传递参数指定
                .authorizedGrantTypes("password", "refresh_token")
                // 客户端的权限范围，此处配置为all全部即可
                .scopes("all")
        ;


    }

    /**
     * 认证服务端点配置。⽤来配置令牌（token）的访问端点和令牌服务(token services)
     * 认证服务器是玩转token的，那么这⾥配置token令牌管理相关（token此时就是⼀个字符串，当下的token需要在服务器端存储，那么存储在哪⾥呢？都是在这⾥配置）
     *
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        super.configure(endpoints);
        endpoints.tokenStore(tokenStore())//指定token的存储方式
                .tokenServices(authorizationServerTokenServices())//token服务的一个描述，可以认为是token生成细节的描述，比如有效时间是多少等
                .authenticationManager(authenticationManager)//指定认证管理器，随后注入一个当前类使用即可
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST);
    }


    /**
     * 生成 TokenStore，令牌存储对象，token的存储方式
     *
     * @return
     */
    @Bean
    public TokenStore tokenStore() {
        return new InMemoryTokenStore();
    }


    @Bean
    public AuthorizationServerTokenServices authorizationServerTokenServices() {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore());
        defaultTokenServices.setSupportRefreshToken(true);//设置支持刷新令牌
        defaultTokenServices.setAccessTokenValiditySeconds(20);//设置令牌有效时间（⼀般设置为2个⼩时），access_token 是我们请求资源需要携带的令牌
        defaultTokenServices.setRefreshTokenValiditySeconds(24 * 3600);// 设置刷新令牌的有效时间，可以设置长一些，一天、两天
        return defaultTokenServices;
    }
}
