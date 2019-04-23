package com.lion.canvas.path.common;


import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.ValidationException;

/**
 * @author
 * @date 2019/3/12
 * 捕获运行时异常
 */
@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler({Exception.class})
    @ResponseBody
    public ResultBean exceptionHandle(HttpServletRequest req, Exception e, BindingResult result) {
         e.printStackTrace();
         if (result.hasErrors()){
             ObjectError oe = result.getAllErrors().get(0);
             return ResultBean.error(new ValidationException(oe.getDefaultMessage()));
         }
        log.error(req.getRequestURL().toString() + "\t" + e.getMessage());
        return ResultBean.error(e);
    }
}