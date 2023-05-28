package com.es.phoneshop.model.entity;

import java.io.Serializable;

public interface Entity<T> extends Serializable {

    T getId();
}
