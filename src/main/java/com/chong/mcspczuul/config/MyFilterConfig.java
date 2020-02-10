package com.chong.mcspczuul.config;

import com.chong.mcspczuul.filter.LogTimeFilter;
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
        registration.setUrlPatterns(new ArrayList<>(Arrays.asList("/*")));
        return registration;
    }

    @Bean
    public LogTimeFilter LogTimeFilter() {
        return new LogTimeFilter();
    }
}
