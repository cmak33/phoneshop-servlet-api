package com.es.phoneshop.model.validator.localDateValidator;

import java.time.LocalDate;
import java.util.Map;

public class DeliveryDateValidator implements LocalDateValidator {

    private static volatile DeliveryDateValidator instance;

    private DeliveryDateValidator() {
    }

    public static DeliveryDateValidator getInstance() {
        if (instance == null) {
            synchronized (DeliveryDateValidator.class) {
                if (instance == null) {
                    instance = new DeliveryDateValidator();
                }
            }
        }
        return instance;
    }

    @Override
    public boolean validate(LocalDate localeDate, Map<String, String> errors, String dateParameterName) {
        if (!(localeDate.isAfter(LocalDate.now()) || localeDate.equals(LocalDate.now()))) {
            errors.put(dateParameterName, "Value can't be past date");
        }
        return true;
    }
}
