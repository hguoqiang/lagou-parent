package com.lagou.edu.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;


/**
 * @description: ⾃定义GateWay全局过滤器时，我们实现Global Filter接⼝即可，通过全局过滤器可以实现⿊⽩名单、限流等功能。
 * 定义全局过滤器，会对所有路由⽣效。
 * @author: huangguoqiang
 * @create: 2022-03-28 17:16
 **/
@Component
@Slf4j
public class BlackListFilter implements GlobalFilter, Ordered {

    // 模拟⿊名单（实际可以去数据库或者redis中查询）
    private static List<String> blackList = new ArrayList<>();

    static {
        blackList.add("0:0:0:0:0:0:0:1"); // 模拟本机地址
    }

    /**
     * 过滤器核⼼⽅法
     *
     * @param exchange 封装了request和response对象的上下⽂
     * @param chain    ⽹关过滤器链（包含全局过滤器和单路由过滤器）
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 思路：获取客户端ip，判断是否在⿊名单中，在的话就拒绝访问，不在的话就放⾏
        ServerHttpRequest request = exchange.getRequest();
        String hostString = request.getRemoteAddress().getHostString();

        ServerHttpResponse response = exchange.getResponse();


        if (blackList.contains(hostString)) {
            log.warn("ip: " +hostString + "在黑名单中，拒绝访问" );
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            String data = "Request be denied";
            DataBuffer dataBuffer = response.bufferFactory().wrap(data.getBytes());
            return response.writeWith(Mono.just(dataBuffer));
        }
        //放行
        return chain.filter(exchange);
    }

    /**
     * 返回值表示当前过滤器的顺序(优先级)，数值越⼩，优先级越⾼
     *
     * @return
     */
    @Override
    public int getOrder() {
        return 0;
    }
}
