package com.chong.mcspczuul.controller;

import com.chong.common.entity.ResponseData;
import com.chong.common.util.ResponseUtil;
import com.chong.mcspczuul.Holder.ThreadLocalHolder;
import com.chong.mcspczuul.entity.ProfileEntity;
import com.chong.mcspczuul.entity.ThreadLocalParamEntity;
import com.chong.mcspczuul.service.ZuulService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
 * 测试当ThreadLocal的使用。
 * 测试Profile的使用。
 *
 * */
@RestController
@RequestMapping("/local")
public class McSpcZuulController {

    private final static Logger logger = LoggerFactory.getLogger(McSpcZuulController.class);

    @Autowired
    private ZuulService zuulService;

    @Autowired
    private ProfileEntity profileEntity;

    @GetMapping("/threadlocal/{threadLocalEnable}")
    public ResponseData getThreadLocalConfig(@PathVariable("threadLocalEnable") String threadLocalEnable){
          if("true".equals(threadLocalEnable)
                  || "false".equals(threadLocalEnable)){

              // 往ThreadLocal里添加实体，初始化Entity的Enable与否
              ThreadLocalParamEntity entity = new ThreadLocalParamEntity();
              entity.setEnable(Boolean.valueOf(threadLocalEnable));
              ThreadLocalHolder.getCurrentThreadLocal().set(entity);
              zuulService.getZuulConfig();

              // 从ThreadLocal中获取Entity，并拿到处理消息
              entity = ThreadLocalHolder.getCurrentThreadLocal().get();
              ThreadLocalHolder.getCurrentThreadLocal().remove();

              return ResponseUtil.success(threadLocalEnable,entity);
          }

        return ResponseUtil.fail("完蛋了，没有使用线程本地变量!");
    }

    @GetMapping("/profile")
    public ResponseData getProfileName(){
       return ResponseUtil.success("profileName 获取成功",profileEntity);
    }

}
