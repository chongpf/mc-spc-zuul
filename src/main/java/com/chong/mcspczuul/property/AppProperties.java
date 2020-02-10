package com.chong.mcspczuul.property;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AppProperties {

    @Autowired
    private Environment environment;

    public String getProperty(String key) {
        return environment.getProperty(key, String.class);
    }

    public String getInstanceId() {
        return environment.getProperty("eureka.instance.instance-id", String.class);
    }

    public List<String> getWhiteApiList() {
        List<String> whiteApiList = new ArrayList<>();
        String whiteStr = environment.getProperty("mc.spc.zuul.whiteApi", String.class);
        if (StringUtils.isNotBlank(whiteStr)) {
            whiteApiList = Arrays.asList(whiteStr.split(","));
        } else {
            whiteApiList.add("/local/login");
            whiteApiList.add("/local/homepage");
        }
        return whiteApiList;
    }
}
