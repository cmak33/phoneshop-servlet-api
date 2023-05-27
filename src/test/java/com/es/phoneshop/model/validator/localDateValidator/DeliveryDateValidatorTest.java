package com.es.phoneshop.model.validator.localDateValidator;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DeliveryDateValidatorTest {

    private final DeliveryDateValidator validator = DeliveryDateValidator.getInstance();
    private Map<String, String> errors;

    @Before
    public void setup() {
        errors = new HashMap<>();
    }

    @Test
    public void givenPastDate_whenValidate_thenAddError() {
        String parameterName = "name";
        LocalDate date = LocalDate.now().minusDays(1);

        boolean actual = validator.validate(date, errors, parameterName);

        assertFalse(actual);
        assertEquals(1, errors.size());
        assertTrue(errors.containsKey(parameterName));
    }

    @Test
    public void givenFutureDate_whenValidate_thenReturnTrue() {
        LocalDate date = LocalDate.now().plusDays(1);
        String name = "name";

        boolean actual = validator.validate(date, errors, name);

        assertTrue(actual);
    }
}
