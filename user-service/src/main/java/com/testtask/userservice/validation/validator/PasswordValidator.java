package com.testtask.userservice.validation.validator;

import com.testtask.userservice.exceptions.custom.InvalidPasswordException;
import com.testtask.userservice.validation.annotation.ValidPassword;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang.StringUtils;

import java.util.regex.Pattern;

public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {
    private static final String PASSWORD_REGEX = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$";
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_REGEX);

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (StringUtils.isBlank(password)) {
            throw new InvalidPasswordException("Password cannot be blank");
        }

        boolean isValid = PASSWORD_PATTERN.matcher(password).matches() && password.length() <= 40;

        if (!isValid) {
            throw new InvalidPasswordException("Password length must be between 8 and 40");
        }

        return true;
    }
}
