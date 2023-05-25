package com.es.phoneshop.model.validator;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class QuantityValidatorTest {

    private final QuantityValidator quantityValidator = QuantityValidator.getInstance();
    private final Locale locale = Locale.ENGLISH;
    private Map<Long, String> errors;

    @Before
    public void setup() {
        errors = new HashMap<>();
    }

    @Test
    public void givenZeroString_whenValidate_thenAddErrorToMap() {
        String notNumberString = "0";
        Long id = 1L;

        boolean actual = quantityValidator.validate(notNumberString, locale, errors, id);

        assertFalse(actual);
        assertEquals(1, errors.size());
        assertTrue(errors.containsKey(id));
    }

    @Test
    public void givenValidValue_whenValidate_thenReturnTrue() {
        String validValue = "12345";
        Long id = 1L;

        boolean actual = quantityValidator.validate(validValue, locale, errors, id);

        assertTrue(actual);
        assertTrue(errors.isEmpty());
    }

    @Test
    public void givenValidValueWithGroupSeparator_whenValidate_thenReturnTrue() {
        String validValue = "1,235";
        Long id = 1L;

        boolean actual = quantityValidator.validate(validValue, locale, errors, id);

        assertTrue(actual);
        assertTrue(errors.isEmpty());
    }

    @Test
    public void givenNotNumberString_whenValidate_thenAddErrorToMap() {
        String notNumberString = "string";
        Long id = 1L;

        boolean actual = quantityValidator.validate(notNumberString, locale, errors, id);

        assertFalse(actual);
        assertEquals(1, errors.size());
        assertTrue(errors.containsKey(id));
    }

    @Test
    public void givenNegativeNumberString_whenValidate_thenAddErrorToMap() {
        String negativeNumberString = "-1";
        Long id = 1L;

        boolean actual = quantityValidator.validate(negativeNumberString, locale, errors, id);

        assertFalse(actual);
        assertEquals(1, errors.size());
        assertTrue(errors.containsKey(id));
    }
}
