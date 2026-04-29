package com.projects.self.system_design.url_shortner.customValidations.validators;

import com.projects.self.system_design.url_shortner.customValidations.validations.Base62Encoded;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class Base62EncodedValidator implements ConstraintValidator<Base62Encoded, String> {
    @Override
    public void initialize(Base62Encoded constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String shortCode, ConstraintValidatorContext constraintValidatorContext) {
        for (int i = 0; i < shortCode.length(); i++) {
            char c = shortCode.charAt(i);
            if (!((c >= '0' && c <= '9') || (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'))) {
                return false;
            }
        }
        return true;
    }
}
