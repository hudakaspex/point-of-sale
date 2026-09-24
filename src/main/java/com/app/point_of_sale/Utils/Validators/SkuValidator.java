package com.app.point_of_sale.Utils.Validators;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented()
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SkuConstraintValidator.class)
public @interface SkuValidator {
    String message() default "SKU is not valid";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {}; 
}
