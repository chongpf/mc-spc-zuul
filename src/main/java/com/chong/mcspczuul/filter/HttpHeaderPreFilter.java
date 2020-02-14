package com.chong.mcspczuul.filter;

import com.chong.common.entity.JwtResult;
import com.chong.common.util.JwtUtil;
import com.chong.common.util.ResponseUtil;
import com.chong.mcspczuul.message.MessageEnum;
import com.chong.mcspczuul.property.AppProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class HttpHeaderPreFilter extends ZuulFilter {

    private static final Logger logger = LoggerFactory.getLogger(HttpHeaderPreFilter.class);

    @Override
    public boolean shouldFilter() {
        boolean result = true;
        if(RequestContext.getCurrentContext().containsKey("shouldFilter")){
            result = (boolean)RequestContext.getCurrentContext().get("shouldFilter");
        }
        return result;
    }

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 3;
    }

    @Override
    public Object run() {
        logger.info("HttpHeaderPreFilter is run");
        try {
            RequestContext context = RequestContext.getCurrentContext();
            String jwt = context.getZuulRequestHeaders().get("token");
            JwtResult jwtResult = JwtUtil.checkToken(jwt);
            if (jwtResult.isSuccess()) {
                context.addZuulRequestHeader("uid", jwtResult.getClaims().getId());
                return null;
            } else {
                ObjectMapper mapper = new ObjectMapper();
                context.setSendZuulResponse(false);
                context.set("sendForwardFilter.ran", true);
                context.setResponseBody(mapper.writeValueAsString(ResponseUtil.fail(MessageEnum.ERROR_NO_AUTH.getMessage())));
                context.getResponse().setContentType("application/json;charset=utf-8");
                context.set("shouldFilter", false);
                return null;
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            ex.printStackTrace();
        }
        return null;
    }
}
