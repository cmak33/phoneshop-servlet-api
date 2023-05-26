package com.es.phoneshop.model.validator.parameterValidator;

import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public class NotBlankParameterValidator implements ParameterValidator {

    private static volatile NotBlankParameterValidator instance;

    private NotBlankParameterValidator() {
    }

    public static NotBlankParameterValidator getInstance() {
        if (instance == null) {
            synchronized (NotBlankParameterValidator.class) {
                if (instance == null) {
                    instance = new NotBlankParameterValidator();
                }
            }
        }
        return instance;
    }

    @Override
    public boolean validate(String value, Map<String, String> errors, String parameterName) {
        if (StringUtils.isBlank(value)) {
            errors.put(parameterName, "Value should not be empty");
            return false;
        }
        return true;
    }
}
