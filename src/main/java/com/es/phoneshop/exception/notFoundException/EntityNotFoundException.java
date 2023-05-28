package com.es.phoneshop.exception.notFoundException;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(Long id, String entityName) {
        super(String.format("%s with id %d was not found", entityName, id));
    }
}
