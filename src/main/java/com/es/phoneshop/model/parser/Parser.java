package com.es.phoneshop.model.parser;


import com.es.phoneshop.exception.CustomParseException;

public interface Parser<T> {

    T parse(String str) throws CustomParseException;
}
