package com.es.phoneshop.model.product.search.filters;

import com.es.phoneshop.model.product.Product;

import java.util.function.BiPredicate;

public abstract class DescriptionFilter implements BiPredicate<Product, String> {

    @Override
    public boolean test(Product product, String description) {
        String productDescription = product.getDescription().toLowerCase();
        description = description.toLowerCase();

        String[] productDescriptionWords = productDescription.split("\\s+");
        String[] descriptionWords = description.split("\\s+");

        return test(productDescriptionWords, descriptionWords);
    }

    protected abstract boolean test(String[] originalDescriptionWords, String[] searchWords);
}
