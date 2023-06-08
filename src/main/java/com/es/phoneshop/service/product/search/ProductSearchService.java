package com.es.phoneshop.service.product.search;

import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.search.SearchCriteria;

import java.util.List;

public interface ProductSearchService {

    List<Product> search(SearchCriteria searchCriteria);
}
