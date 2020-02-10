package com.chong.mcspczuul.controller;

import com.chong.common.entity.ResponseData;
import com.chong.common.util.DateUtil;
import com.chong.common.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/*
 * 测试当Filter，ZuulFilter，Interceptor，aspect，controller的执行顺序。
 * 执行顺序如下：
 * 0.filter(init)
 * 1.filter(doFilter before FilterChian.doFilter)
 * 2.ZuulFilter(type=pre,run())
 * 3.Interceptor(@preHandle)
 * 4.aspect(@arround before proceeding.proceed())
 * 5.aspect(@before)
 * 6.controller.method(controller method)
 * 7.aspect(@arround after proceeding.proceed())
 * 8.aspect(@afterReturning)
 * 9.Interceptor(@postHandle)
 * 10.inteceptor(@afterCompletion)
 * 11.ZuulFilter(type=post,run())
 * 12.filter(doFilter after FilterChain.doFilter)
 * 13.filter(doDestory)
 * */
@RestController
@RequestMapping("/local")
public class FilterInterceptorAspectOrderController {

    private final static Logger logger = LoggerFactory.getLogger(FilterInterceptorAspectOrderController.class);

    @GetMapping("/time")
     public ResponseData time(@RequestParam("param") String param){
      String result = param+DateUtil.getDateTime();
      logger.info("拿到了宝贵的时间"+result);
      return ResponseUtil.success("拿到了宝贵的时间",result);
    }

}
