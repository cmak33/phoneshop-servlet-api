package com.es.phoneshop.validator.parameterValidator;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.Map;

public class SearchPriceValidator implements ParameterValidator {

    private static volatile SearchPriceValidator instance;

    private SearchPriceValidator() {
    }

    public static SearchPriceValidator getInstance() {
        if (instance == null) {
            synchronized (SearchPriceValidator.class) {
                if (instance == null) {
                    instance = new SearchPriceValidator();
                }
            }
        }
        return instance;
    }

    @Override
    public boolean validate(String value, Map<String, String> errors, String parameterName) {
        if (StringUtils.isBlank(value)) {
            return true;
        }
        BigDecimal price;
        try {
            price = new BigDecimal(value);
        } catch (NumberFormatException numberFormatException) {
            errors.put(parameterName, "Value is not valid price");
            return false;
        }
        if (price.compareTo(BigDecimal.ZERO) < 0) {
            errors.put(parameterName, "Price cannot be negative");
            return false;
        }
        return true;
    }
}
