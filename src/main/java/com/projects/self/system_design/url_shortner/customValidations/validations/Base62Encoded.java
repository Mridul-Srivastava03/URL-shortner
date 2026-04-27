package com.projects.self.system_design.url_shortner.customValidations.validations;

import com.projects.self.system_design.url_shortner.customValidations.validators.Base62EncodedValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {Base62EncodedValidator.class})
public @interface Base62Encoded {
    String message() default "Enter valid Short Code";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
