package com.reclamos.utils;

import java.util.Set;
import java.util.stream.Collectors;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class ValidationUtil {

  private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
  private static final Validator validator = factory.getValidator();

  public static <T> Set<ConstraintViolation<T>> validate(T object) {
    return validator.validate(object);
  }

  public static <T> String getErrorMessage(Set<ConstraintViolation<T>> violations) {
    return violations.stream()
      .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
      .collect(Collectors.joining(", "));
  }

  public static <T> boolean isValid(T object) {
    return validate(object).isEmpty();
  }
}
