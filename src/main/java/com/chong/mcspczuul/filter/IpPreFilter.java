package com.chong.mcspczuul.filter;

import com.chong.common.util.DateUtil;
import com.chong.common.util.ResponseUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public class IpPreFilter extends ZuulFilter {

    private final static Logger logger = LoggerFactory.getLogger(IpPreFilter.class);

    private List<String> blackIpList = Arrays.asList(new String[]{"127.0.0.1", "127.0.0.11"});

    public IpPreFilter() {
        super();
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public Object run() throws ZuulException{
        try {
            ObjectMapper mapper = new ObjectMapper();
            String message1="ipPreFilter-1101msg:this is a black Ip===";
            String message2="ipPreFilter-1102msg:this is a error Ip===";
            String message3="ipPreFilter-1103msg:this is a normal Ip===";

            String ctxmsg1="ctx-ipPreFilter run black ip===";
            String ctxmsg2="ctx-ipPreFilter run error ip===";
            String ctxmsg3="ctx-ipPreFilter run normal ip===";

            logger.info("Start: this is ipPreFilter");

            RequestContext context = RequestContext.getCurrentContext();
            String fromIp = context.getRequest().getRemoteAddr();
            String param=(String)context.getRequest().getParameter("ipPreFilter");
            if (blackIpList.contains(fromIp)&& StringUtils.isBlank(param)) {
                logger.info(ctxmsg1+"--"+ DateUtil.getDateTime());
                context.set("preFilterResult", ctxmsg1);
                context.setSendZuulResponse(false);
                context.setResponseBody(mapper.writeValueAsString(ResponseUtil.fail(message1)));
                // context.setResponseBody("message: it-is-a-black-ip");
                context.getResponse().setContentType("application/json;charset=utf-8");
                context.set("shouldFilter",false);
            } else if(blackIpList.contains(fromIp)&& StringUtils.isNotBlank(param)){
                logger.info(ctxmsg2+"--"+DateUtil.getDateTime());
                context.set("preFilterResult", ctxmsg2);
                throw new ZuulException(message2,1003,"=error ip cause.=");
            } else {
                context.set("preFilterResult", ctxmsg3);
                logger.info(message3+"--"+DateUtil.getDateTime());
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

}
