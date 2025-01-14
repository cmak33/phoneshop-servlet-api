package com.es.phoneshop.model.attributesHolder;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class HttpSessionAttributesHolder implements AttributesHolder {

    private final HttpSession httpSession;

    @Override
    public Object getAttribute(String name) {
        synchronized (httpSession) {
            return httpSession.getAttribute(name);
        }
    }

    @Override
    public void setAttribute(String name, Object obj) {
        synchronized (httpSession) {
            httpSession.setAttribute(name, obj);
        }
    }

    @Override
    public Object getSynchronizationObject() {
        return httpSession;
    }
}
