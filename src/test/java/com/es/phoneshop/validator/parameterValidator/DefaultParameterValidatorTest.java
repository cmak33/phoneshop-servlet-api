package com.es.phoneshop.validator.parameterValidator;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DefaultParameterValidatorTest {

    private final DefaultParameterValidator validator = DefaultParameterValidator.getInstance();
    private Map<String, String> errors;

    @Before
    public void setup() {
        errors = new HashMap<>();
    }

    @Test
    public void givenNullParameter_whenValidate_thenAddError() {
        String parameterName = "name";

        boolean actual = validator.validate(null, errors, parameterName);

        assertFalse(actual);
        assertEquals(1, errors.size());
        assertTrue(errors.containsKey(parameterName));
    }

    @Test
    public void givenNotBlankParameter_whenValidate_thenReturnTrue() {
        String parameter = "valid parameter";
        String name = "name";

        boolean actual = validator.validate(parameter, errors, name);

        assertTrue(actual);
    }
}
