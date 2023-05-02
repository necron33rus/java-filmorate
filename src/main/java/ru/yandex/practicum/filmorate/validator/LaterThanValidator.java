package ru.yandex.practicum.filmorate.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class LaterThanValidator implements ConstraintValidator<LaterThan, LocalDate> {
    public static final LocalDate FIRST_RELEASE_DATE = LocalDate.of(1895, 12, 28);

    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext constraintValidatorContext) {
        if (localDate != null) {
            return !localDate.isBefore(FIRST_RELEASE_DATE);
        }
        return false;
    }
}
