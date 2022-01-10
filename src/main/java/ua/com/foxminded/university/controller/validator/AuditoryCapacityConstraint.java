package ua.com.foxminded.university.controller.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = AuditoryCapacityValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface AuditoryCapacityConstraint {
    String message() default "Capacity should be in range 20 and 50";  
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
