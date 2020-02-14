package com.chong.mcspczuul.controller;

import com.chong.common.entity.ResponseData;
import com.chong.common.util.ResponseUtil;
import com.chong.mcspczuul.annotation.ApiOperationLimit;
import com.chong.mcspczuul.filter.LimitPreFilter;
import com.chong.mcspczuul.property.AppProperties;
import com.google.common.util.concurrent.RateLimiter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/local")
@Api(description = "获取spring-cloud-config-server通过git管理的配置信息")
public class SpcConfigPropertyController {

    private static final Logger logger = LoggerFactory.getLogger(SpcConfigPropertyController.class);

    @Autowired
    AppProperties appProperties;

    @GetMapping("/property")
    @ApiOperation(value = "全部配置信息", notes = "git提交后，务必执行method=Post,/actuator/refresh",response = ResponseData.class)
    @ApiOperationLimit(value = "normal")
    public ResponseData getProperties() {
        logger.info("controller:requestpath=property run");
        return ResponseUtil.success(appProperties);
    }

    @GetMapping("/property/{pName}")
    @ApiOperation(value = "获取指定属性的值", notes = "获取指定属性的值",response = ResponseData.class)
    @ApiImplicitParams({@ApiImplicitParam(name = "pName", required = true, paramType = "query", dataType = "String")})
    public ResponseData getProperty(@PathVariable("pName") String pName) {
        logger.info("controller:requestpath=property/"+pName+" run");
        return ResponseUtil.success(appProperties.getProperty(pName));
    }

    @PostMapping("/property/limitrate/{rate}")
    @ApiOperationLimit(value = "normal")
    public ResponseData  setLimitRate(@PathVariable("rate") double rate){
        logger.info("controller:requestpath=property/limitrate/"+rate+" run");
        if(rate==0){rate=100;}
        LimitPreFilter.rateLimiter = RateLimiter.create(rate);
        return ResponseUtil.success(rate);
    }
    @GetMapping("/property/limitrate")
    @ApiOperationLimit(value = "normal")
    public ResponseData  getLimitRate(){
        logger.info("controller:requestpath=property/limitrate/  run");
        return ResponseUtil.success(LimitPreFilter.rateLimiter);
    }
}
