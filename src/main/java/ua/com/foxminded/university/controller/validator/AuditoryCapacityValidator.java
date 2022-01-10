package ua.com.foxminded.university.controller.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AuditoryCapacityValidator implements ConstraintValidator<AuditoryCapacityConstraint, Integer> {
    @Override
    public void initialize(AuditoryCapacityConstraint constraintAnnotation) {
        
    }
    
    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        return value.intValue() >= 20 && value.intValue() <= 50;
    }
}
