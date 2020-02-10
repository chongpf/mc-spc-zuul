package com.chong.mcspczuul.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ErrorFilter extends ZuulFilter {

    private final static Logger logger = LoggerFactory.getLogger(ErrorFilter.class);

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
        return "error";
    }

    @Override
    public Object run() {
        try {
            String message1 = "errorFilter-1301msg:this is a error without expected.===";
            String ctxmsg1 = "ctx-errorFilter run ===";

            RequestContext context = RequestContext.getCurrentContext();
            context.setSendZuulResponse(false);
            if (StringUtils.isBlank(context.getResponseBody())) {
                context.setResponseBody(message1);
                context.getResponse().setContentType("application/json;charset=utf-8");
            }

            String preHandleResult = (String) context.get("preFilterResult");
            context.set("preFilterResult", preHandleResult + "  " +ctxmsg1);

           logger.info((String) context.get("preFilterResult"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;

    }
}
