package com.core.aspect;

import com.alibaba.fastjson.JSON;
import com.core.annotation.AfterLogIgnore;
import com.core.annotation.BeforeLogIgnore;
import com.core.constant.Global;
import com.core.model.BaseCommand;
import com.core.util.DateUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;


@Aspect
@Order(-2)
@Component
public class GlobalRequestAspect {

    private final static Logger logger = LoggerFactory.getLogger(GlobalRequestAspect.class);

    @Value("${spring.application.name}")
    private String applicationName;

    @Pointcut("execution(public * com.innjoy.pms.*.controller.*.*(..)))")
    public void log() {

    }

    @Before("log()")
    public void doBefore(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        BeforeLogIgnore beforeLogIgnore = method.getAnnotation(BeforeLogIgnore.class);
        if (beforeLogIgnore == null) {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();

            StringBuilder stringBuffer = new StringBuilder();
            stringBuffer.append(applicationName);
            stringBuffer.append(" ").append(DateUtil.getTimeStr());
            stringBuffer.append(" " + Global.LOG_PARA_URL + ":").append(request.getRequestURI());
            Object[] objects = joinPoint.getArgs();
            for (Object object : objects) {
                if (object instanceof BaseCommand) {
                    stringBuffer.append(" " + Global.LOG_PARA_PARAS + ":").append(JSON.toJSONString(object));
                }
            }
            logger.info(stringBuffer.toString());
        }
    }

    @AfterReturning(pointcut = "log()", returning = "object")//打印输出结果
    public void doAfterReturning(JoinPoint joinPoint, Object object) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        AfterLogIgnore afterLogIgnore = method.getAnnotation(AfterLogIgnore.class);
        if (afterLogIgnore == null) {
            String stringBuffer = (applicationName) +
                    " " + DateUtil.getTimeStr() +
                    " " + Global.LOG_PARA_RESPONSE + ":" + JSON.toJSONString(object);
            logger.info(stringBuffer);
        }
    }
}
