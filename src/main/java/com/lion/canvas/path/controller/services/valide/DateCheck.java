package com.lion.canvas.path.controller.services.valide;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author
 * @date 2019/3/14
 */
@Target({ElementType.METHOD,ElementType.FIELD})
@Retention(value = RetentionPolicy.RUNTIME)
@Constraint(validatedBy= DateCheckImpl.class)
public @interface DateCheck {
    String message();
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
