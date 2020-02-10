package com.chong.mcspczuul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@SpringBootApplication
@EnableDiscoveryClient
@EnableZuulProxy
public class McSpcZuulApplication {

    public static void main(String[] args) {
        SpringApplication.run(McSpcZuulApplication.class, args);
    }

}
