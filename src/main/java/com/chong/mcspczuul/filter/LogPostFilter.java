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
        return 4;
    }

    @Override
    public String filterType() {
        return "post";
    }

    @Override
    public Object run() {
        String ctxmsg1="ctx-logPostFilter run ===";
        RequestContext context = RequestContext.getCurrentContext();
        String preHandleResult = (String)context.get("preFilterResult");
        context.set("preFilterResult", preHandleResult+"==="+ctxmsg1);
        logger.info(context.get("preFilterResult")+"--"+ DateUtil.getDateTime());

        return null;
    }
}
