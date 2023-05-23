package com.es.phoneshop.model.parser;

import org.junit.Test;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class QuantityValidatorTest {

    private final QuantityValidator quantityValidator = QuantityValidator.getInstance();
    private final Locale locale = Locale.ENGLISH;

    @Test
    public void givenValidValue_whenParse_thenReturnParsedValue() {
        int value = 100;

        Optional<Integer> actual = quantityValidator.tryParse(new HashMap<>(), 1L, locale, String.valueOf(value));

        assertTrue(actual.isPresent());
        assertEquals(value, (int) actual.get());
    }

    @Test
    public void givenInvalidValue_whenParse_thenReturnOptionalEmpty() {
        String invalidValue = "invalid value";
        Map<Long, String> errors = new HashMap<>();
        Long id = 1L;

        Optional<Integer> quantity = quantityValidator.tryParse(errors, id, locale, invalidValue);

        assertTrue(quantity.isEmpty());
        assertTrue(errors.containsKey(id));
    }

    @Test
    public void givenNegativeValue_whenParse_thenReturnOptionalEmpty() {
        int value = -1;
        Map<Long, String> errors = new HashMap<>();
        Long id = 1L;

        Optional<Integer> quantity = quantityValidator.tryParse(errors, id, locale, String.valueOf(value));

        assertTrue(quantity.isEmpty());
        assertTrue(errors.containsKey(id));
    }
}
