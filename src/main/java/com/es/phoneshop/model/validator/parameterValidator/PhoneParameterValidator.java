package com.es.phoneshop.model.validator.parameterValidator;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.util.Map;

public class PhoneParameterValidator implements ParameterValidator {

    private static volatile PhoneParameterValidator instance;
    private final PhoneNumberUtil phoneNumberUtil;

    private PhoneParameterValidator() {
        this.phoneNumberUtil = PhoneNumberUtil.getInstance();
    }

    public static PhoneParameterValidator getInstance() {
        if (instance == null) {
            synchronized (PhoneParameterValidator.class) {
                if (instance == null) {
                    instance = new PhoneParameterValidator();
                }
            }
        }
        return instance;
    }

    @Override
    public boolean validate(String value, Map<String, String> errors, String parameterName) {
        String PHONE_PATTERN = "[+]?[\\d\\s]+";
        if (value.matches(PHONE_PATTERN)) {
            try {
                Phonenumber.PhoneNumber number = phoneNumberUtil.parse(value, null);
                if (phoneNumberUtil.isValidNumber(number)) {
                    return true;
                }
            } catch (NumberParseException ignored) {
            }
        }
        errors.put(parameterName, "Value is not valid phone number");
        return false;
    }
}
