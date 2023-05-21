package com.es.phoneshop.model.parser;

import com.es.phoneshop.exception.CustomParseException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class QuantityParserTest {

    private final QuantityParser quantityParser = new QuantityParser();

    @Test
    public void givenValidValue_whenParse_thenReturnParsedValue() throws CustomParseException {
        int value = 100;

        int actual = quantityParser.parse(String.valueOf(value));

        assertEquals(value, actual);
    }

    @Test(expected = CustomParseException.class)
    public void givenInvalidValue_whenParse_thenThrowCustomParseException() throws CustomParseException {
        String invalidValue = "invalid value";

        quantityParser.parse(invalidValue);
    }

    @Test(expected = CustomParseException.class)
    public void givenNegativeValue_whenParse_thenThrowCustomParseException() throws CustomParseException {
        int value = -1;

        quantityParser.parse(String.valueOf(value));
    }
}
