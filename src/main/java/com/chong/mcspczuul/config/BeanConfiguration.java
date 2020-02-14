package com.chong.mcspczuul.config;

import com.chong.mcspczuul.aspect.ControllerTraceAspect;
import com.chong.mcspczuul.entity.ProfileEntity;
import com.chong.mcspczuul.filter.*;
import com.chong.mcspczuul.property.AppProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class BeanConfiguration {

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
