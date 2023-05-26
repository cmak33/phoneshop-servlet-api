package com.es.phoneshop.model.validator.parameterValidator;

import java.util.Map;

public interface ParameterValidator {

    boolean validate(String value, Map<String, String> errors, String parameterName);
}
