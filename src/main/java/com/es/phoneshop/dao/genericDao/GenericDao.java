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

@Getter(AccessLevel.PROTECTED)
@Setter
public class GenericDao<T extends Entity> implements Dao<T> {

    private final Lock readLock;
    private final Lock writeLock;
    private List<T> entityList;

    public GenericDao() {
        entityList = new ArrayList<>();
        ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
        readLock = readWriteLock.readLock();
        writeLock = readWriteLock.writeLock();
    }

    @Override
    public Optional<T> getEntity(Long id) {
        if (id != null) {
            readLock.lock();
            try {
                return entityList.stream()
                        .filter(entity -> id.equals(entity.getId()))
                        .findAny();
            } finally {
                readLock.unlock();
            }
        }
        return Optional.empty();
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
