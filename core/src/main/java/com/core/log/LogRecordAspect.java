package com.core.log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.core.annotation.LogRecord;
import com.core.util.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogRecordAspect {

    private String keyWord;
    private String className;
    private String methodName;
    private String content;

    private static final Logger logger = LoggerFactory.getLogger(LogRecordAspect.class);

    @Pointcut("@annotation(com.innjoy.saas.common.annotation.LogRecord)")
    public void log() {

    }

    @AfterReturning("log()")
    public Object changeDataSource(JoinPoint jp) {
        logger.debug("返回后通知（After returning advice）并记录日志!");

        MethodSignature sign = (MethodSignature) jp.getSignature();
        className = jp.getTarget().getClass().getName();
        methodName = jp.getSignature().getName();
        boolean flag = sign.getMethod().isAnnotationPresent(LogRecord.class);
        keyWord = methodName;
        if (flag) {
            keyWord += "(" + sign.getMethod().getAnnotation(LogRecord.class).describe() + ")";
        }
        content = StringUtils.getString("类名: ", className, "  ", "方法名: ", keyWord, "  ", "传入参数: ", parseParames(jp.getArgs()), "\n");

        logger.info(content);
        return null;
    }

    /**
     * 解析方法参数
     *
     * @param parames 方法参数
     * @return 解析后的方法参数
     */
    private String parseParames(Object[] parames) {
        ObjectMapper om = new ObjectMapper();
        try {
            return om.writeValueAsString(parames);
        } catch (Exception e) {
            logger.error("记录系统运行异常日志时解析传入 参数出错：" + ExceptionUtils.getFullStackTrace(e));
            return "解析传入 参数出错：" + ExceptionUtils.getFullStackTrace(e);
        }
    }
}
