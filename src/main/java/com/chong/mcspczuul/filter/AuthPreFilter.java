package com.chong.mcspczuul.filter;

import com.chong.common.entity.JwtResult;
import com.chong.common.util.JwtUtil;
import com.chong.common.util.ResponseUtil;
import com.chong.mcspczuul.message.MessageEnum;
import com.chong.mcspczuul.property.AppProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * 验证用户是否拥有访问URL的权限.
 * */
public class AuthPreFilter extends ZuulFilter {

    private static final Logger logger = LoggerFactory.getLogger(AuthPreFilter.class);

    @Autowired
    private AppProperties appProperties;

    @Override
    public boolean shouldFilter() {
        boolean result = true;
        if(RequestContext.getCurrentContext().containsKey("shouldFilter")){
            result = (boolean)RequestContext.getCurrentContext().get("shouldFilter");
        }
        return result;
    }

    @Override
    public int filterOrder() {
        return 2;
    }

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public Object run() {
        try {
            logger.info("开始权限认证filter");
            boolean hasAuth = false;
            RequestContext context = RequestContext.getCurrentContext();
            ObjectMapper mapper = new ObjectMapper();

            //1.白名单判断：通过配置文件获取url白名单,如/login.
            List<String> whiteApiList = appProperties.getWhiteApiList();
            if (whiteApiList != null) {
                //直接命中白名单api
                if (whiteApiList.contains(context.getRequest().getRequestURI())) {
                    logger.info("符合白名单列表，通过认证:" + context.getRequest().getRequestURI());
                    context.set("shouldFilter",false);
                    return null;
                }
                //通过正则表达式匹配path api.如/order/{orderId}
                for (String whiteApi : whiteApiList) {
                    // 白名单转换为正则表达式：/order/{orderId} ==> /order/[a-zA-Z0-9]+
                    String regex = whiteApi.replaceAll("\\/\\{[a-zA-Z0-9]+\\}", "\\/[a-zA-Z0-9]+");
                    Pattern pattern = Pattern.compile(regex);
                    Matcher matcher = pattern.matcher(context.getRequest().getRequestURI());
                    if (matcher.matches()) {
                        logger.info("符合白名单列表，通过认证:" + context.getRequest().getRequestURI());
                        context.set("shouldFilter",false);
                        return null;
                    }
                }
            }

            // todo:2.1 判断用户的token是否存在
            String jwt = "";
            String openJwtCheck = appProperties.getProperty("mc-spc-zuul.openJwtCheck");
            if (StringUtils.isNotBlank(openJwtCheck) && Boolean.parseBoolean(openJwtCheck)) {
                jwt = context.getRequest().getHeader("token");
                if (StringUtils.isBlank(jwt)) {
                    context.setSendZuulResponse(false);
                    context.set("sendForwardFilter.ran", true);
                    context.setResponseBody(mapper.writeValueAsString(ResponseUtil.fail(MessageEnum.ERROR_NO_AUTH.getMessage())));
                    context.getResponse().setContentType("application/json;charset=utf-8");
                    context.set("shouldFilter",false);
                    return null;
                }
                JwtResult jwtResult = JwtUtil.checkToken(jwt);
                if (!jwtResult.isSuccess()) {
                    context.setSendZuulResponse(false);
                    context.set("sendForwardFilter.ran", true);
                    context.setResponseBody(mapper.writeValueAsString(ResponseUtil.fail(MessageEnum.getMessage(jwtResult.getErrCode()))));
                    context.getResponse().setContentType("application/json;charset=utf-8");
                    context.set("shouldFilter",false);
                    return null;
                }

            } else {
                // 没有开启token认证时，做一个demo的token设置到消息头.
                String testUid = appProperties.getProperty("mc-spc-common.testUid");
                jwt = JwtUtil.getToken(testUid);
                context.addZuulRequestHeader("token",jwt);
            }
            // todo:3.从持久化数据或者缓存获取当前用户拥有的权限列表，并进行当前访问url是否在权限列表中
            // 如果有访问列表权限 return null;持久化对接前设置为true
            hasAuth=true;

            // 4.没有访问url的权限时,进行验证失败的反馈处理.
            if (!hasAuth) {
                logger.info("你没有访问权限:" + context.getRequest().getRequestURI());
                context.setSendZuulResponse(false);
                context.set("sendForwardFilter.ran", true);
                context.setResponseBody(mapper.writeValueAsString(ResponseUtil.fail(MessageEnum.ERROR_NO_AUTH.getMessage())));
                context.getResponse().setContentType("application/json;charset=utf-8");
                context.set("shouldFilter",false);
            }
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
