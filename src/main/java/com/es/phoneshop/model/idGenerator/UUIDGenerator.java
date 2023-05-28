package com.es.phoneshop.model.idGenerator;

import java.util.UUID;

public class UUIDGenerator implements IdGenerator {

    private static volatile UUIDGenerator instance;

    private UUIDGenerator() {
    }

    public static UUIDGenerator getInstance() {
        if (instance == null) {
            synchronized (UUIDGenerator.class) {
                if (instance == null) {
                    instance = new UUIDGenerator();
                }
            }
        }
        return instance;
    }

    @Override
    public Long generateId() {
        return UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
    }
}
