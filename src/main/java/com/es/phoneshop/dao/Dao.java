package com.es.phoneshop.dao;

import com.es.phoneshop.model.entity.Entity;

import java.util.Optional;

public interface Dao<T extends Entity> {

    Optional<T> getEntity(Long id);

    void save(T entity);
}
