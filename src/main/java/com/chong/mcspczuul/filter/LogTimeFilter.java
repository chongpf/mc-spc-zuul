package com.chong.mcspczuul.filter;

import com.chong.common.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import java.io.IOException;

public class LogTimeFilter implements Filter {
    private final static Logger logger = LoggerFactory.getLogger(LogTimeFilter.class);

    private String message1 = "LogTimeFilter init";
    private String message21 = "LogTimeFilter before doFilter";
    private String message22 = "LogTimeFilter after doFilter";
    private String message3 = "LogTimeFilter destory";

    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info(message1 + DateUtil.getDateTime());
    }

    public void doFilter(ServletRequest var1, ServletResponse var2, FilterChain var3)
            throws IOException, ServletException {
        logger.info(message21 + DateUtil.getDateTime());
        logger.info(var1.getParameter("param") + DateUtil.getDateTime());

        var3.doFilter(var1, var2);

        logger.info(message22 + DateUtil.getDateTime());
        logger.info(var2.getOutputStream().toString() + DateUtil.getDateTime());
    }

    public void destroy() {
        logger.info(message3 + DateUtil.getDateTime());
    }
}
