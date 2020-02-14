package com.chong.mcspczuul.property;

import com.chong.mcspczuul.entity.OperationLimitEntity;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(value = "operationlimit")
public class AppOperationLimitProperty {

    private boolean enable;

    private double defaultLimit;

    private List<OperationLimitEntity> list;

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public double getDefaultLimit() {
        return defaultLimit;
    }

    public void setDefaultLimit(double defaultLimit) {
        this.defaultLimit = defaultLimit;
    }

    public List<OperationLimitEntity> getList() {
        return list;
    }

    public void setList(List<OperationLimitEntity> list) {
        this.list = list;
    }
}
