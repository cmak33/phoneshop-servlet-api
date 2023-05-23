package com.es.phoneshop.model.parser;

import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public class QuantityValidator {

    private static volatile QuantityValidator instance;

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

    public Optional<Integer> tryParse(Map<Long, String> errors, Long id, Locale locale, String str) {
        NumberFormat numberFormat = NumberFormat.getInstance(locale);
        ParsePosition pos = new ParsePosition(0);
        Number quantityNumber = numberFormat.parse(str, pos);
        if (quantityNumber == null || pos.getIndex() != str.length()) {
            errors.put(id, "Quantity was not a number");
        } else {
            int quantity = quantityNumber.intValue();
            if (quantity > 0) {
                return Optional.of(quantity);
            }
            errors.put(id, "Quantity should be bigger than zero");
        }
        return Optional.empty();
    }
}
