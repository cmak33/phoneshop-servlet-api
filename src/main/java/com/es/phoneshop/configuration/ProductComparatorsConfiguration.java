package com.es.phoneshop.configuration;

import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.sorting.SortField;
import com.es.phoneshop.model.product.sorting.SortOrder;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class ProductComparatorsConfiguration {
    private static ProductComparatorsConfiguration instance;
    private Map<SortField, Comparator<Product>> ascendingComparators;


    private ProductComparatorsConfiguration() {
        this.ascendingComparators = createComparators();
    }

    public static ProductComparatorsConfiguration getInstance() {
        if (instance == null) {
            synchronized (ProductComparatorsConfiguration.class) {
                if (instance == null) {
                    instance = new ProductComparatorsConfiguration();
                }
            }
        }
        return instance;
    }

    private Map<SortField, Comparator<Product>> createComparators() {
        Map<SortField, Comparator<Product>> comparators = new HashMap<>();
        comparators.put(SortField.DESCRIPTION, Comparator.comparing(Product::getDescription));
        comparators.put(SortField.PRICE, Comparator.comparing(Product::getPrice));
        return comparators;
    }

    public Comparator<Product> getComparatorByFieldAndOrder(SortField sortField, SortOrder sortOrder) {
        Comparator<Product> productComparator = ascendingComparators.get(sortField);
        return sortOrder.equals(SortOrder.ASCENDING) ? productComparator : productComparator.reversed();
    }
}
