package com.chong.mcspczuul.filter;

import com.chong.mcspczuul.entity.ServiceLimitEntity;
import com.chong.mcspczuul.property.AppProperties;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class ServiceDownGradeRouteFilter extends ZuulFilter {

    private static final Logger logger = LoggerFactory.getLogger(ServiceDownGradeRouteFilter.class);

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
        return 4;
    }

    @Override
    public String filterType() {
        return "route";
    }

    @Override
    public Object run() {
        try {
            logger.info("this is down grade service.wait down grade...");
            String downGradeSetting = appProperties.getProperty("servicedowngrade.enable");
            boolean isDownGrade = StringUtils.isNotBlank(downGradeSetting) && Boolean.parseBoolean(downGradeSetting);
            if (!isDownGrade) {
                return null;
            }
            String serviceids = appProperties.getProperty("servicedowngrade.serviceids");
            if (StringUtils.isBlank(serviceids)) {
                return null;
            }
            String[] downGradeServices = serviceids.split(",");
            RequestContext context = RequestContext.getCurrentContext();
            String currentService = context.getRequest().getRequestURI().split("/")[1];
            for (String downGradeService : downGradeServices) {
                if (StringUtils.equals(currentService, downGradeService)) {
                    logger.info("Service down grade started");
                    context.setSendZuulResponse(false);
                    context.set("shouldFilter", false);
                    context.set("sendForwardFilter.ran", true);
                    context.setResponseBody("服务降级中，请稍后！");
                    context.getResponse().setContentType("application/json;charset=utf-8");
                    logger.info("LimitFilet redis clusterRateLimit 100.00 is run：" +
                            RequestContext.getCurrentContext().getRequest().getRequestURL());
                    logger.info("grade down service:" + currentService);
                    logger.info("Service down grade end");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        logger.info("leave service down grade");
        return null;
    }
}
