package com.chong.mcspczuul.controller;

import com.chong.common.util.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(description = "用户登录控制器")
@RestController
@RequestMapping("/local")
public class ZuulLoginController {

    private static final Logger logger = LoggerFactory.getLogger(ZuulLoginController.class);

    @GetMapping("/login")
    @ApiOperation(value = "登录", notes = "登录方法")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", paramType = "query",
                    dataType = "String", required = true)
    })
    public String login(@RequestParam("uid") String uid) {
        //todo: 通过持久化数据判断用户ID是否存在，如果存在返回token
        String token = JwtUtil.getToken(uid);
        return token;
    }
}
