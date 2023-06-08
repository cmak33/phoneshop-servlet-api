package com.es.phoneshop.configuration;

import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.search.SearchType;
import com.es.phoneshop.model.product.search.filters.AllWordsFilter;
import com.es.phoneshop.model.product.search.filters.AnyWordFilter;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiPredicate;

public class ProductDescriptionFiltersConfiguration {

    private static volatile ProductDescriptionFiltersConfiguration instance;
    private final Map<SearchType, BiPredicate<Product, String>> filters;

    private ProductDescriptionFiltersConfiguration() {
        filters = createFilters();
    }

    public static ProductDescriptionFiltersConfiguration getInstance() {
        if (instance == null) {
            synchronized (ProductDescriptionFiltersConfiguration.class) {
                if (instance == null) {
                    instance = new ProductDescriptionFiltersConfiguration();
                }
            }
        }
        return instance;
    }

    private Map<SearchType, BiPredicate<Product, String>> createFilters() {
        HashMap<SearchType, BiPredicate<Product, String>> result = new HashMap<>();
        result.put(SearchType.ALL_WORDS, new AllWordsFilter());
        result.put(SearchType.ANY_WORD, new AnyWordFilter());
        return result;
    }

    public BiPredicate<Product, String> getFilterBySearchType(SearchType searchType) {
        return filters.get(searchType);
    }
}
