package com.es.phoneshop.model.paymentMethod;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public enum PaymentMethod {

    CASH("Cash"), CREDIT_CARD("Credit card");

    private final String methodName;

    PaymentMethod(String methodName) {
        this.methodName = methodName;
    }

    public static PaymentMethod parseFromMethodName(String str) {
        str = str.toLowerCase(Locale.ROOT);
        return switch (str) {
            case "cash" -> CASH;
            case "credit card" -> CREDIT_CARD;
            default -> null;
        };
    }

    public static List<String> getMethodNames() {
        return Arrays.stream(PaymentMethod.values()).map(x -> x.methodName).toList();
    }
}
