package com.testtask.userservice.validation.validator;

import com.testtask.userservice.exceptions.custom.InvalidEmailException;
import com.testtask.userservice.validation.annotation.ValidEmail;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.validator.routines.EmailValidator;

import java.util.Objects;

@Log4j2
public class CustomEmailValidator implements ConstraintValidator<ValidEmail, String> {

    @Override
    public boolean isValid(String field, ConstraintValidatorContext context) {
        if (Objects.isNull(field)) {
            log.warn("Email is in null or empty format");
            throw new InvalidEmailException("Email address cannot be null or empty");
        }
        boolean valid = EmailValidator.getInstance().isValid(field);

        if (!valid) {
            log.warn("Email - {} is in a incorrect format", field);
            throw new InvalidEmailException("Invalid email address format, please provide a valid email address (test@gmail.com, adc@gmail.com, etc)");
        }
        return true;
    }
}