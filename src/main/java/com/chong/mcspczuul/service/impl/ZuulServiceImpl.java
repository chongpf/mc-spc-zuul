package com.chong.mcspczuul.service.impl;

import com.chong.mcspczuul.Holder.ThreadLocalHolder;
import com.chong.mcspczuul.entity.ThreadLocalParamEntity;
import com.chong.mcspczuul.service.ZuulService;
import org.springframework.stereotype.Service;

@Service
public class ZuulServiceImpl implements ZuulService {

    @Override
    public void getZuulConfig() {
        ThreadLocalParamEntity entity = ThreadLocalHolder.getCurrentThreadLocal().get();
        if (entity != null) {
            if (entity.getEnable()) {
                entity.setMessage("开启了线程本地变量！返回结果：你真棒！");
            } else {
                entity.setMessage("没有开启了线程本地变量！返回结果：美女别走！");
            }
            ThreadLocalHolder.getCurrentThreadLocal().set(entity);
        }
    }

}
