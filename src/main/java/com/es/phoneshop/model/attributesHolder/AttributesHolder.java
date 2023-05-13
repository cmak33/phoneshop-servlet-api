package com.es.phoneshop.model.attributesHolder;

public interface AttributesHolder {

    Object getAttribute(String name);

    void setAttribute(String name, Object obj);
}
