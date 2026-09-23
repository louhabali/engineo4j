package com.cinema.gateway.filter;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.Refill;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

@Component
public class RateLimitingFilter implements GlobalFilter, Ordered {

    private final ProxyManager<String> proxyManager;

    public RateLimitingFilter(ProxyManager<String> proxyManager) {
        this.proxyManager = proxyManager;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest req = exchange.getRequest();

        String key = req.getHeaders().getFirst("X-User-Id");
        if (key == null || key.isBlank()) {
            key = req.getRemoteAddress() != null ? req.getRemoteAddress().getAddress().getHostAddress() : "anon";
        }

        // Fetch bucket using key and a supplier for configuration
        Bucket bucket = proxyManager.getProxy("rl:" + key, () -> getConfig());

        return Mono.fromCallable(() -> bucket.tryConsume(1))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(allowed -> {
                    if (Boolean.TRUE.equals(allowed)) {
                        return chain.filter(exchange);
                    }
                    exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
                    return exchange.getResponse().setComplete();
                });
    }

    private BucketConfiguration getConfig() {
        return BucketConfiguration.builder()
                .addLimit(Bandwidth.classic(20, Refill.greedy(20, Duration.ofMinutes(1))))
                .build();
    }

    @Override
    public int getOrder() {
        return -2;
    }
}