package com.chong.mcspczuul.interceptor;

import com.chong.common.util.DateUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.core.instrument.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TimeInterceptor implements HandlerInterceptor {
    private final static Logger logger = LoggerFactory.getLogger(TimeInterceptor.class);
    private String message1 = "TimeInterceptor preHandle";
    private String message2 = "TimeInterceptor postHandle";
    private String message3 = "TimeInterceptor afterCompletion";
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.info(message1+DateUtil.getDateTime());
//        logger.info(request.getParameter("param")+DateUtil.getDateTime());
        return true;
    }
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
//        logger.info(message2+DateUtil.getDateTime());
//        ObjectMapper mapper = new ObjectMapper();
////        response.getWriter().println();
//        logger.info("PostHandle"+DateUtil.getDateTime());
    }
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
//        logger.info(message3+DateUtil.getDateTime());
//        logger.info(handler.toString()+DateUtil.getDateTime());
    }
}
