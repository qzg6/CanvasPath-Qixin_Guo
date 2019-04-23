package com.lion.canvas.path.common;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * @author
 * @date 2019/3/13
 * 日志打印&调用时间
 *
 */
@Slf4j
@Component
@Aspect
public class ControllerAop {

    @Around(value = "execution(public com.lion.canvas..path.common.ResultBean *(..))")
    public Object handlerControllerMethod(ProceedingJoinPoint pjp) {
        long startTime = System.currentTimeMillis();
        ResultBean<?> result;
        try {
            result = (ResultBean<?>) pjp.proceed();
            log.info(pjp.getSignature() + " use time:" + (System.currentTimeMillis() - startTime) +"ms");
        } catch (Throwable e) {
            result = handlerException(pjp, e);
        }
        return result;
    }

    private ResultBean<?> handlerException(ProceedingJoinPoint pjp, Throwable e) {
        ResultBean<?> result = new ResultBean();
        log.error(pjp.getSignature() + " error ", e);
        result.setMsg(e.toString());
        result.setCode(ResultBean.FAIL);
        return result;
    }
}
