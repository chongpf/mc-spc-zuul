package com.chong.mcspczuul.config;

import com.chong.mcspczuul.annotation.SetOperationLimit;
import com.chong.mcspczuul.aspect.SetOperationLimitAspect;
import com.chong.mcspczuul.entity.OperationLimitEntity;
import com.chong.mcspczuul.property.AppOperationLimitProperty;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

@Component
public class OperationLimitAppCtxAware implements ApplicationContextAware {

    @Autowired
    private AppOperationLimitProperty operationLimitProperty;

    private static final Logger logger = LoggerFactory.getLogger(OperationLimitAppCtxAware.class);

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        logger.info("start load OperationLimitSemphore from @ApiOperationLimit and application property's operationlimit");
        try {
            if (!operationLimitProperty.isEnable()) {
                return;
            }
            Map<String, Object> beans = applicationContext.getBeansWithAnnotation(RestController.class);
            if (beans == null) {
                return;
            }
            Iterator<String> keys = beans.keySet().iterator();
            List<OperationLimitEntity> operationLimitEntityList = operationLimitProperty.getList();
            while (keys.hasNext()) {
                String classname = keys.next();
                Class<?> clazz = beans.get(classname).getClass();
                if(clazz.getName().contains("EnhancerBySpringCGLib")||clazz.getName().contains("$$")){
                    classname = clazz.getName().split("\\$\\$")[0];
                    clazz = java.lang.Class.forName(classname);
                }
                Method[] methods = clazz.getDeclaredMethods();
                if (methods == null) {
                    continue;
                }
                for (Method method : methods) {
                    if (method.isAnnotationPresent(SetOperationLimit.class)) {
                        SetOperationLimit annotation = method.getAnnotation(SetOperationLimit.class);
                        String limitKey = StringUtils.isBlank(annotation.value()) ? "defaultLimit" : annotation.value();
                        double limitRate = operationLimitProperty.getDefaultLimit();
                        if (operationLimitEntityList != null) {
                            for (OperationLimitEntity entity : operationLimitEntityList) {
                                if (StringUtils.equals(limitKey, entity.getOperationId())) {
                                    limitKey = entity.getOperationId();
                                    limitRate = entity.getOperationRate();
                                    break;
                                }
                            }
                        }
                        if (!SetOperationLimitAspect.semphoreMap.containsKey(limitKey)) {
                            SetOperationLimitAspect.semphoreMap.put(limitKey, new Semaphore(new Double(limitRate).intValue()));
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        logger.info("set OperationLimitSemphore end");
    }
}
