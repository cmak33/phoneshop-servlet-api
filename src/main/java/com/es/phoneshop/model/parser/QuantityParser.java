package com.es.phoneshop.model.parser;

import com.es.phoneshop.exception.CustomParseException;
import lombok.Setter;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

@Setter
public class QuantityParser implements Parser<Integer> {

    private Locale locale = Locale.ENGLISH;

    @Override
    public Integer parse(String str) throws CustomParseException {
        NumberFormat numberFormat = NumberFormat.getInstance(locale);
        int quantity;
        try {
            quantity = numberFormat.parse(str).intValue();
        } catch (ParseException exception) {
            throw new CustomParseException("Quantity was not a number");
        }
        if (quantity <= 0) {
            throw new CustomParseException("Quantity should be bigger than zero");
        }
        return quantity;
    }
}
