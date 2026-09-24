package com.app.point_of_sale.Utils.Validators;

import java.util.regex.Pattern;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class SkuConstraintValidator implements ConstraintValidator<SkuValidator, String> {
    private static final Pattern SKU_PATTERN = Pattern.compile("^[A-Za-z0-9_-]+$");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.trim().isEmpty()) {
            return true;
        }
        return SKU_PATTERN.matcher(value).matches();
    }
}
