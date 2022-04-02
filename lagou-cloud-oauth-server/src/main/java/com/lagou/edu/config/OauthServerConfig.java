package com.lagou.edu.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.jwt.crypto.sign.MacSigner;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import javax.sql.DataSource;

/**
 * @description: 当前类为Oauth2 server的配置类（需要继承特定的⽗类AuthorizationServerConfigurerAdapter）
 * @author: huangguoqiang
 * @create: 2022-03-30 21:35
 **/
@Configuration
@EnableAuthorizationServer//开启认证服务器功能
public class OauthServerConfig extends AuthorizationServerConfigurerAdapter {

    //jwt 签名密钥
    //@Value("${jwt.signing.key}")
    private String jwtSigningKey = "hsjkewj0980a787d";


    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private DataSource dataSource;


    @Autowired
    private LagouAccessTokenConvertor lagouAccessTokenConvertor;


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
        //从内存中加载Oauth2客户端
        /*clients.inMemory()//客户端信息存储在什么地方，可以是内存、数据库等
                .withClient("client_test")//添加一个client配置，指定client_id
                .secret("test123")//指定客户端的密码
                .resourceIds("autodeliver")//指定客户端所能访问的资源id清单，此处的资源id也需要在资源服务器配置一样的
                //认证类型、令牌颁发模式，可以配置多个，但不一定都能用到，具体使用哪种方式，需要客户端调用时候传递参数指定
                .authorizedGrantTypes("password", "refresh_token")
                // 客户端的权限范围，此处配置为all全部即可
                .scopes("all")
        ;*/

        //从数据库加载Oauth2客户端信息
        clients.withClientDetails(clientDetailsService());

    }

    @Bean
    public JdbcClientDetailsService clientDetailsService() {
        return new JdbcClientDetailsService(dataSource);
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

    @Bean
    public AuthorizationServerTokenServices authorizationServerTokenServices() {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore());
        defaultTokenServices.setSupportRefreshToken(true);//设置支持刷新令牌
        defaultTokenServices.setAccessTokenValiditySeconds(20);//设置令牌有效时间（⼀般设置为2个⼩时），access_token 是我们请求资源需要携带的令牌
        defaultTokenServices.setRefreshTokenValiditySeconds(24 * 3600);// 设置刷新令牌的有效时间，可以设置长一些，一天、两天

        //针对 jwt令牌的添加
        defaultTokenServices.setTokenEnhancer(jwtAccessTokenConverter());
        return defaultTokenServices;
    }
}
