package com.chong.mcspczuul.filter;

import com.chong.common.util.DateUtil;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogPostFilter extends ZuulFilter {
    private final static Logger logger = LoggerFactory.getLogger(LogPostFilter.class);

    public LogPostFilter() {
        super();
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public int filterOrder() {
        return 5;
    }

    @Override
    public String filterType() {
        return "post";
    }

    @Override
    public Object run() {
        String ctxmsg1="ctx-logPostFilter run ===";
        logger.info(ctxmsg1+"responseBody:"+RequestContext.getCurrentContext().getResponseBody());
        return null;
    }
}
