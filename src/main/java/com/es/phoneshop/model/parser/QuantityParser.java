package com.es.phoneshop.model.parser;

import com.es.phoneshop.exception.CustomParseException;

import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Locale;

public class QuantityParser implements Parser<Integer> {

    private static volatile QuantityParser instance;

    private QuantityParser() {
    }

    public static QuantityParser getInstance() {
        if (instance == null) {
            synchronized (QuantityParser.class) {
                if (instance == null) {
                    instance = new QuantityParser();
                }
            }
        }
        return instance;
    }

    @Override
    public Integer parse(String str, Locale locale) throws CustomParseException {
        NumberFormat numberFormat = NumberFormat.getInstance(locale);
        ParsePosition pos = new ParsePosition(0);
        Number quantityNumber = numberFormat.parse(str, pos);
        if (quantityNumber == null || pos.getIndex() != str.length()) {
            throw new CustomParseException(String.format("Could not parse quantity %s to number", str));
        }
        long quantity = quantityNumber.longValue();
        if (!isInIntegerBounds(quantity)) {
            throw new CustomParseException(String.format("Quantity %s is out of bounds", str));
        }
        return quantityNumber.intValue();
    }

    private boolean isInIntegerBounds(long value) {
        return value <= Integer.MAX_VALUE && value >= Integer.MIN_VALUE;
    }
}
