package com.es.phoneshop.model.product;

public record ProductDescriptionMatch(Product product, int productDescriptionWordsCount,
                                      int matchingWordsCount) {
}
