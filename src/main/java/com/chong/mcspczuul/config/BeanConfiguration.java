package com.chong.mcspczuul.config;

import com.chong.mcspczuul.aspect.ControllerTraceAspect;
import com.chong.mcspczuul.entity.ProfileEntity;
import com.chong.mcspczuul.filter.AuthPreFilter;
import com.chong.mcspczuul.filter.HttpHeaderPreFilter;
import com.chong.mcspczuul.filter.IpPreFilter;
import com.chong.mcspczuul.filter.LogPostFilter;
import com.chong.mcspczuul.property.AppProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class BeanConfiguration {

    @Bean
    public AppProperties appProperties(){
        return  new AppProperties();
    }

    @Bean
    public IpPreFilter ipPreFilter() {
        return new IpPreFilter();
    }

    @Bean
    public AuthPreFilter authPreFilter(){
        return new AuthPreFilter();
    }

    @Bean
    public LogPostFilter logPostFilter(){return new LogPostFilter();}

    @Bean
    public HttpHeaderPreFilter httpHeaderPreFilter (){return new HttpHeaderPreFilter();}
//   // 当Controller实现了ErrorController接口后，发生异常时，error会在error方法里执行，返回值ResponseData会反馈给调用方。
//   //  errorFilter会执行，但是context.setResponseBody不会反馈给调用方
//    @Bean
//    public ErrorFilter errorFilter(){
//        return new ErrorFilter();
//    }

    @Bean
    public ControllerTraceAspect timeAspect(){
        return new ControllerTraceAspect();
    }

    @Profile("pro")
    @Bean
    public ProfileEntity profileEntityPro(){
        ProfileEntity entity = new ProfileEntity();
        entity.setProfileName("pro");
        return entity;
    }

    @Profile("dev")
    @Bean
    public ProfileEntity profileEntityDev(){
        ProfileEntity entity = new ProfileEntity();
        entity.setProfileName("dev");
        return entity;
    }
}
