package com.lion.canvas.path.controller.services.valide;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * @author
 * @date 2019/3/14
 * 日期验证
 */
public class DateCheckImpl implements ConstraintValidator<DateCheck, Object> {
    @Override
    public void initialize(DateCheck constraintAnnotation) {

    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        if (o instanceof String){
            try {
                //yyyy-MM-dd
                LocalDate.parse(o.toString());
                return true;
            }catch (DateTimeParseException e){
            }
        }
        return false;
    }
}
