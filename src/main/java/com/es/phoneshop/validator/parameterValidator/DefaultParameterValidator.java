package com.es.phoneshop.validator.parameterValidator;

import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public class DefaultParameterValidator implements ParameterValidator {

    private static volatile DefaultParameterValidator instance;

    private DefaultParameterValidator() {
    }

    public static DefaultParameterValidator getInstance() {
        if (instance == null) {
            synchronized (DefaultParameterValidator.class) {
                if (instance == null) {
                    instance = new DefaultParameterValidator();
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
