package com.es.phoneshop.model.parser;

import com.es.phoneshop.exception.QuantityParseException;

import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Locale;

public class QuantityParser implements Parser<Integer> {

    @Override
    public Integer parse(Locale locale, String str) throws QuantityParseException {
        NumberFormat numberFormat = NumberFormat.getInstance(locale);
        ParsePosition pos = new ParsePosition(0);
        Number quantityNumber = numberFormat.parse(str, pos);
        if (quantityNumber == null || pos.getIndex() != str.length()) {
            throw new QuantityParseException("Quantity was not a number");
        }
        int quantity = quantityNumber.intValue();
        if (quantity <= 0) {
            throw new QuantityParseException("Quantity should be bigger than zero");
        }
        return quantity;
    }
}
