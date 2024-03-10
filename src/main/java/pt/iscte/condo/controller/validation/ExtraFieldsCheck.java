package pt.iscte.condo.controller.validation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ExtraFieldsCheckValidator.class)
public @interface ExtraFieldsCheck {
    String message() default "If extraDate is not null, extraStartTime and extraEndTime can't be null.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
