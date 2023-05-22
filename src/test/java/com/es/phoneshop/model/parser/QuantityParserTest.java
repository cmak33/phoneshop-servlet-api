package com.es.phoneshop.model.parser;

import com.es.phoneshop.exception.CustomParseException;
import com.es.phoneshop.exception.QuantityParseException;
import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.assertEquals;

public class QuantityParserTest {

    private final QuantityParser quantityParser = new QuantityParser();
    private final Locale locale = Locale.ENGLISH;

    @Test
    public void givenValidValue_whenParse_thenReturnParsedValue() throws CustomParseException {
        int value = 100;

        int actual = quantityParser.parse(locale, String.valueOf(value));

        assertEquals(value, actual);
    }

    @Test(expected = QuantityParseException.class)
    public void givenInvalidValue_whenParse_thenThrowQuantityParseException() throws QuantityParseException {
        String invalidValue = "invalid value";

        quantityParser.parse(locale, invalidValue);
    }

    @Test(expected = QuantityParseException.class)
    public void givenNegativeValue_whenParse_thenThrowQuantityParseException() throws QuantityParseException {
        int value = -1;

        quantityParser.parse(locale, String.valueOf(value));
    }
}
