package com.es.phoneshop.model.validator.validator;

import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Map;

public class QuantityValidator implements Validator {

    private static volatile QuantityValidator instance;
    private static final String NEGATIVE_NUMBER_OR_ZERO_PATTERN = "^-[0-9]\\d*|0$";

    private QuantityValidator() {
    }

    public static QuantityValidator getInstance() {
        if (instance == null) {
            synchronized (QuantityValidator.class) {
                if (instance == null) {
                    instance = new QuantityValidator();
                }
            }
        }
        return instance;
    }

    @Override
    public boolean validate(String str, Locale locale, Map<Long, String> errors, Long id) {
        if (str.matches(createPattern(locale))) {
            return true;
        } else if (str.matches(NEGATIVE_NUMBER_OR_ZERO_PATTERN)) {
            errors.put(id, "Quantity should be bigger than zero");
        } else {
            errors.put(id, "Quantity was not a number");
        }
        return false;
    }

    private String createPattern(Locale locale) {
        DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance(locale);
        char groupSeparator = symbols.getGroupingSeparator();
        return String.format("[1-9](\\d{0,2}((%1$s\\d{3})*)|\\d*)", groupSeparator);
    }
}
