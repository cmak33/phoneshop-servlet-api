package com.es.phoneshop.model.product.search.filters;

import java.util.Arrays;

public class AllWordsFilter extends DescriptionFilter {

    @Override
    protected boolean test(String[] originalDescriptionWords, String[] searchWords) {
        return Arrays.asList(originalDescriptionWords)
                .containsAll(Arrays.asList(searchWords));
    }
}
