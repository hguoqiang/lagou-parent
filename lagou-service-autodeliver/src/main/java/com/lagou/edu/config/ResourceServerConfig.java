package com.lagou.edu.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.jwt.crypto.sign.MacSigner;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

/**
 * @description:
 * @author: huangguoqiang
 * @create: 2022-03-31 20:36
 **/
@Configuration
@EnableResourceServer//开启资源服务器功能
@EnableWebSecurity //开启web访问安全功能,访问接口的安全
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {


    //jwt 签名密钥
    //@Value("${jwt.signing.key}")
    private String jwtSigningKey = "hsjkewj0980a787d";

    @Autowired
    private LagouAccessTokenConvertor lagouAccessTokenConvertor;

    /**
     * 该方法用于资源服务器向远程服务器发起请求，进行token校验。/oauth/check_token
     *
     * @param resources
     * @throws Exception
     */
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {

       /*
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
        */

        //jwt 令牌改造 ，不需要和远程认证服务器交互，添加本地tokenStore
        resources.resourceId("autodeliver")
                .tokenStore(tokenStore())
                .stateless(true);//设置无状态


    }

    /**
     * 生成 TokenStore，令牌存储对象，token的存储方式
     *
     * @return
     */
    @Bean
    public TokenStore tokenStore() {
        //return new InMemoryTokenStore();

        /*
        使用JWT格式token,JSON Web Token（JWT）是⼀个开放的⾏业标准（RFC 7519）,
        JWT可以使⽤HMAC算法或使⽤RSA的公钥/私钥对来签名，防⽌被篡改。
        JWT令牌由三部分组成:JWT令牌由三部分组成：Header、Payload、Signature。
        第三部分是签名，此部分⽤于防⽌jwt内容被篡改。 这个部分使⽤base64url将
        前两部分进⾏编码，编码后使⽤点（.）连接组成字符串，最后使⽤header中声
        明签名算法进⾏签名。
        HMACSHA256(
            base64UrlEncode(header) + "." +
            base64UrlEncode(payload),
            secret)
            base64UrlEncode(header)： jwt令牌的第⼀部分。
            base64UrlEncode(payload)： jwt令牌的第⼆部分。
            secret：签名所使⽤的密钥
         */

        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    /**
     * 返回jwt令牌转换器（帮助我们⽣成jwt令牌的）。
     * jwt加密签名 我们使用非对称密钥，在这⾥，我们可以把签名密钥传递进去给转换器对象
     *
     * @return
     */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        jwtAccessTokenConverter.setSigningKey(jwtSigningKey);// 签名密钥
        jwtAccessTokenConverter.setVerifier(new MacSigner(jwtSigningKey));// 验证时使⽤的密钥，和签名密钥保持⼀致
        jwtAccessTokenConverter.setAccessTokenConverter(lagouAccessTokenConvertor);
        return jwtAccessTokenConverter;
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
