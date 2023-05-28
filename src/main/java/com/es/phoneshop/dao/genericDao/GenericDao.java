package com.es.phoneshop.dao.genericDao;

import com.es.phoneshop.dao.Dao;
import com.es.phoneshop.model.entity.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Function;

@Getter(AccessLevel.PROTECTED)
@Setter
public class GenericDao<A, T extends Entity<A>> implements Dao<A, T> {

    private final Lock readLock;
    private final Lock writeLock;
    private List<T> entityList;

    public GenericDao() {
        entityList = new ArrayList<>();
        ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
        readLock = readWriteLock.readLock();
        writeLock = readWriteLock.writeLock();
    }

    protected <B> Optional<T> getEntityByField(B fieldValue, Function<T, B> valueGetter) {
        if (fieldValue != null) {
            readLock.lock();
            try {
                return entityList.stream()
                        .filter(entity -> fieldValue.equals(valueGetter.apply(entity)))
                        .findAny();
            } finally {
                readLock.unlock();
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<T> getEntity(A id) {
        return getEntityByField(id, Entity::getId);
    }

    @Override
    public void save(T entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity was null");
        }
        writeLock.lock();
        try {
            Optional<T> entityWithSameId = getEntity(entity.getId());
            if (entityWithSameId.isPresent()) {
                int productWithSameIdIndex = entityList.indexOf(entityWithSameId.get());
                entityList.set(productWithSameIdIndex, entity);
            } else {
                entityList.add(entity);
            }
        } finally {
            writeLock.unlock();
        }
    }

    public List<T> getEntityList() {
        return entityList;
    }
}
