package com.chong.mcspczuul.property;

import com.chong.mcspczuul.entity.ServiceLimitEntity;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@ConfigurationProperties(prefix = "servicelimit")
@Component
public class AppServiceLimitProperty {

    private boolean enable;

    private double defaultlimit;

    private List<ServiceLimitEntity> list;

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public double getDefaultlimit() {
        return defaultlimit;
    }

    public void setDefaultlimit(double defaultlimit) {
        this.defaultlimit = defaultlimit;
    }

    public List<ServiceLimitEntity> getList() {
        return list;
    }

    public void setList(List<ServiceLimitEntity> list) {
        this.list = list;
    }
}
