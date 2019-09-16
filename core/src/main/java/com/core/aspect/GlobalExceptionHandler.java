package com.core.aspect;


import com.core.constant.ExceptionEnum;
import com.core.exception.ServiceException;
import com.core.model.ResultBody;
import com.core.util.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


@ControllerAdvice
public class GlobalExceptionHandler {

    @Value("${spring.application.name}")
    private String applicationName;
    private static final String SECURITY_EXCEPTION_MSG = "Access is denied";
    private static final String SECURITY_EXCEPTION_MSG2 ="不允许访问";
    private final static Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResultBody exception(Exception e) {
        if (e instanceof ServiceException) {
            logger.error("[SYSTEM ERROR]",e);
            ServiceException se = (ServiceException) e;
            return ResultUtil.error(se.getCode(), se.getMessage());
        }
        /**正常 instanceof AccessException 就可以，但是目前考虑 saas-common公用给非必要依赖权限的项目，所以只判断国际化为中文，英文的系统**/
        if(e.getMessage().equals(SECURITY_EXCEPTION_MSG) || e.getMessage().equals(SECURITY_EXCEPTION_MSG2)){
            return ResultUtil.error(401, "当前账号的角色权限有修改，如有疑问，请联系系统管理员，谢谢。");
        }

        logger.error("[SYSTEM ERROR]", e);
        return ResultUtil.error(ExceptionEnum.SYSTEM_EXCEPTION.getCode(), ExceptionEnum.SYSTEM_EXCEPTION.getMsg());
    }


    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    @ResponseBody
    public ResultBody httpMessageNotReadableException(HttpMessageNotReadableException se) {
        return ResultUtil.error(ExceptionEnum.INVALID_PARAMETER.getCode(), se.getMessage());
    }
}
