package com.es.phoneshop.model.validator.localDateValidator;

import java.time.LocalDate;
import java.util.Map;

public interface LocalDateValidator {

    boolean validate(LocalDate localeDate, Map<String,String> errors, String dateParameterName);
}
