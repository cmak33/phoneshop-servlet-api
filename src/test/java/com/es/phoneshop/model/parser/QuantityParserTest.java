package com.es.phoneshop.model.parser;

import com.es.phoneshop.exception.CustomParseException;
import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.assertEquals;

public class QuantityParserTest {

    private final QuantityParser quantityValidator = QuantityParser.getInstance();
    private final Locale locale = Locale.ENGLISH;

    @Test
    public void givenValidValue_whenParse_thenReturnParsedValue() throws CustomParseException {
        int value = 100;

        int actual = quantityValidator.parse(String.valueOf(value), locale);

        assertEquals(value, actual);
    }

    @Test(expected = CustomParseException.class)
    public void givenInvalidValue_whenParse_thenReturnOptionalEmpty() throws CustomParseException {
        String invalidValue = "invalid value";

        quantityValidator.parse(invalidValue, locale);
    }

    @Test
    public void givenValidStringWithGroupSeparator_whenParse_thenReturnValue() throws CustomParseException {
        int value = 12345;
        String strValue = "12,345";

        int actual = quantityValidator.parse(strValue, locale);

        assertEquals(value, actual);
    }
}
