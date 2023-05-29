package com.es.phoneshop.validator.parameterValidator;

import java.util.Map;

public interface ParameterValidator {

    boolean validate(String value, Map<String, String> errors, String parameterName);
}
