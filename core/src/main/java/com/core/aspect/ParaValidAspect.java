package com.core.aspect;

import com.core.constant.ExceptionEnum;
import com.core.model.BaseCommand;
import com.core.util.BeanValidatorsUtil;
import com.core.util.ResultUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Map;

@Aspect
@Order(-1)
@Component
public class ParaValidAspect {
    @Around("(execution(public * com.innjoy.pms.*.controller.*.*(..))) && args(..,command)")
    public Object changeDataSource(ProceedingJoinPoint point, BaseCommand command) throws Throwable {
        Object[] args = point.getArgs();
        Validator validator = Validation.buildDefaultValidatorFactory()
                .getValidator();
        Map<String, String> errors = BeanValidatorsUtil.extractPropertyAndMessage(validator.validate(command));
        if (errors != null && errors.size() > 0) {
            return ResultUtil.error(ExceptionEnum.INVALID_PARAMETER, errors);
        }
        return point.proceed(args);
    }
}
