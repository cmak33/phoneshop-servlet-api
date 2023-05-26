package com.es.phoneshop.model.idGenerator;

import com.es.phoneshop.configuration.ProductComparatorsConfiguration;
import com.es.phoneshop.dao.product.CustomProductDao;
import com.es.phoneshop.dao.product.ProductDao;
import com.es.phoneshop.service.product.CustomProductService;

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

    @Override
    public String generateStringId() {
        return UUID.randomUUID().toString();
    }
}
