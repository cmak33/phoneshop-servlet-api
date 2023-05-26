package com.es.phoneshop.model.validator.validator;

import java.util.Locale;
import java.util.Map;

public interface Validator {

    boolean validate(String str, Locale locale, Map<Long, String> errors, Long id);
}
