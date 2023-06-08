package com.es.phoneshop.model.product.search.filters;

import java.util.Arrays;
import java.util.List;

public class AnyWordFilter extends DescriptionFilter {

    @Override
    protected boolean test(String[] originalDescriptionWords, String[] searchWords) {
        List<String> originalWordsList = Arrays.asList(originalDescriptionWords);
        return Arrays.stream(searchWords)
                .anyMatch(originalWordsList::contains);
    }
}
