package com.chong.mcspczuul.config;

import com.chong.mcspczuul.filter.*;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Arrays;

@Configuration
public class MyFilterConfig {

    @Bean
    public FilterRegistrationBean<LogTimeFilter> logTimeFilterRegistrationBean() {
        FilterRegistrationBean<LogTimeFilter> registration = new FilterRegistrationBean<>();
        registration.setOrder(1);
        registration.setFilter(LogTimeFilter());
        registration.setUrlPatterns(new ArrayList<>(Arrays.asList("/api-sales/*","/local/*")));
        return registration;
    }

    @Bean
    public LogTimeFilter LogTimeFilter() {
        return new LogTimeFilter();
    }

    @Bean
    public LimitPreFilter limitPreFilter(){return new LimitPreFilter();}

    @Bean
    public IpPreFilter ipPreFilter() {
        return new IpPreFilter();
    }

    @Bean
    public AuthPreFilter authPreFilter(){
        return new AuthPreFilter();
    }

    @Bean
    public HttpHeaderPreFilter httpHeaderPreFilter (){return new HttpHeaderPreFilter();}

    @Bean
    public ServiceDownGradeRouteFilter serviceDownGradeRouteFilter(){return new ServiceDownGradeRouteFilter();}

    @Bean
    public LogPostFilter logPostFilter(){return new LogPostFilter();}

    //   // 当Controller实现了ErrorController接口后，发生异常时，error会在error方法里执行，返回值ResponseData会反馈给调用方。
//   //  errorFilter会执行，但是context.setResponseBody不会反馈给调用方
//    @Bean
//    public ErrorFilter errorFilter(){
//        return new ErrorFilter();
//    }

}
