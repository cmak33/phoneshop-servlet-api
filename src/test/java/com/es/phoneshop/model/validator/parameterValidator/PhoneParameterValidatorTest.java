package com.es.phoneshop.model.validator.parameterValidator;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PhoneParameterValidatorTest {

    private final PhoneParameterValidator validator = PhoneParameterValidator.getInstance();
    private Map<String, String> errors;

    @Before
    public void setup() {
        errors = new HashMap<>();
    }

    @Test
    public void givenEmptyNumber_whenValidate_thenAddError() {
        String parameterName = "name";
        String phone = "";

        boolean actual = validator.validate(phone, errors, parameterName);

        assertFalse(actual);
        assertEquals(1, errors.size());
        assertTrue(errors.containsKey(parameterName));
    }

    @Test
    public void givenPhoneNumberWithLetters_whenValidate_thenAddError() {
        String parameterName = "name";
        String phone = "+375abc";

        boolean actual = validator.validate(phone, errors, parameterName);

        assertFalse(actual);
        assertEquals(1, errors.size());
        assertTrue(errors.containsKey(parameterName));
    }

    @Test
    public void givenValidPhoneNumber_whenValidate_thenReturnTrue() {
        String parameter = "+375 44 123 4331";
        String name = "name";

        boolean actual = validator.validate(parameter, errors, name);

        assertTrue(actual);
    }

}
