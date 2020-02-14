package com.chong.mcspczuul.filter;

import com.chong.mcspczuul.entity.ServiceLimitEntity;
import com.chong.mcspczuul.property.AppServiceLimitProperty;
import com.google.common.util.concurrent.RateLimiter;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class LimitPreFilter extends ZuulFilter {

    private static final Logger logger = LoggerFactory.getLogger(LimitPreFilter.class);

    // 集群限流
    public static double clusterLimitRate = 100.00;
    // 单节点限流
    public static volatile RateLimiter rateLimiter = RateLimiter.create(100.00);

    // 服务限流，限流对象服务和限流对象服务的速率配置在properties里
    @Autowired
    public AppServiceLimitProperty limitProperty;

    @Autowired
    @Qualifier("longRedisTemplate")
    private RedisTemplate<String, Long> redisTemplate;

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public Object run() {
        RequestContext context = RequestContext.getCurrentContext();
        Long currentSecond = System.currentTimeMillis() / 1000;

        // -----------------以下是集群限流-----------------------
        String key = "mc-spc-zull-" + currentSecond;
        try {
            if (!redisTemplate.hasKey(key)) {
                redisTemplate.opsForValue().set(key, 0L, 10, TimeUnit.SECONDS);
            }
            if (redisTemplate.opsForValue().increment(key, 1L) > clusterLimitRate) {
                context.setSendZuulResponse(false);
                context.set("shouldFilter", false);
                context.set("sendForwardFilter.ran", true);
                context.setResponseBody("访问人数较多，请稍后！");
                context.getResponse().setContentType("application/json;charset=utf-8");
                logger.info("LimitFilet redis clusterRateLimit 100.00 is run：" +
                        RequestContext.getCurrentContext().getRequest().getRequestURL());
                logger.info("limitKey:" + key + "concurrentValue:" + redisTemplate.opsForValue().get(key));
            }
        } catch (Exception ex) {
            // -----------------以下是单节点限流-----------------------
            rateLimiter.acquire();
            logger.info("LimitFilet guava single server RateLimit 100.00 is run：" +
                    RequestContext.getCurrentContext().getRequest().getRequestURL());
        }

        // -----------------以下是服务级别限流-----------------------
        // 获取服务限流的值
        try {
            if(limitProperty.isEnable()) {
                double serviceLimitRate = limitProperty.getDefaultlimit();
                String serviceLimitId = "ServiceLimitId";
                List<ServiceLimitEntity> serviceLimitEntities = limitProperty.getList();
                String requestUri = context.getRequest().getRequestURI();
                if (serviceLimitEntities != null) {
                    for (ServiceLimitEntity serviceLimitEntity : serviceLimitEntities) {
                        requestUri = requestUri.replaceAll("/", "");
                        if (requestUri.startsWith(serviceLimitEntity.getServiceid())) {
                            serviceLimitRate = serviceLimitEntity.getServicerate();
                            serviceLimitId = "ServiceLimitId-" + serviceLimitEntity.getServiceid();
                            break;
                        }
                    }
                }

                String servieLimitKey = serviceLimitId + currentSecond;
                if (!redisTemplate.hasKey(servieLimitKey)) {
                    redisTemplate.opsForValue().set(servieLimitKey, 0L, 10, TimeUnit.SECONDS);
                }
                if (redisTemplate.opsForValue().increment(servieLimitKey, 1) > serviceLimitRate) {
                    context.setSendZuulResponse(false);
                    context.set("shouldFilter", false);
                    context.set("sendForwardFilter.ran", true);
                    context.setResponseBody("访问人数较多，请稍后！");
                    context.getResponse().setContentType("application/json;charset=utf-8");
                    logger.info("LimitFilet redis serviceRateLimit 100.00 is run：" +
                            RequestContext.getCurrentContext().getRequest().getRequestURL());
                    logger.info("limitKey:" + servieLimitKey +
                            "concurrentValue:" + redisTemplate.opsForValue().get(key));

                }
            }
        }catch (Exception ex){
            rateLimiter.acquire();
        }
        return null;
    }
}
