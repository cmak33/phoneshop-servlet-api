package com.es.phoneshop.dao;

import com.es.phoneshop.model.entity.Entity;

import java.util.Optional;

public interface Dao<A, T extends Entity<A>> {

    Optional<T> getEntity(A id);

    void save(T entity);
}
