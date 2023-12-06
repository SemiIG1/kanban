package id.ac.pnj.kanban.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;

import java.time.LocalDateTime;

public class DatesMatchValidator implements ConstraintValidator<DatesMatch, Object> {
    private String startField;
    private String endField;
    @Override
    public void initialize(DatesMatch constraintAnnotation) {
        this.startField = constraintAnnotation.startField();
        this.endField = constraintAnnotation.endField();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
        LocalDateTime startDatetime = (LocalDateTime) new BeanWrapperImpl(value).getPropertyValue(startField);
        LocalDateTime endDatetime = (LocalDateTime) new BeanWrapperImpl(value).getPropertyValue(endField);
        if (startDatetime == null | endDatetime == null) {
            return false;
        }
        return endDatetime.isAfter(startDatetime);



    }
}
