package com.es.phoneshop.model.parser;


import com.es.phoneshop.exception.CustomParseException;

import java.util.Locale;

public interface Parser<T> {

    T parse(Locale locale, String str) throws CustomParseException;
}
