package com.es.phoneshop.service.product.search;

import com.es.phoneshop.configuration.ProductDescriptionFiltersConfiguration;
import com.es.phoneshop.dao.product.CustomProductDao;
import com.es.phoneshop.dao.product.ProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.search.SearchCriteria;
import com.es.phoneshop.model.product.search.SearchType;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Predicate;

public class CustomProductSearchService implements ProductSearchService {

    private static volatile CustomProductSearchService instance;
    private final ProductDao productDao;
    private final ProductDescriptionFiltersConfiguration productDescriptionFiltersConfiguration;

    private CustomProductSearchService() {
        productDao = CustomProductDao.getInstance();
        productDescriptionFiltersConfiguration = ProductDescriptionFiltersConfiguration.getInstance();
    }

    public static CustomProductSearchService getInstance() {
        if (instance == null) {
            synchronized (CustomProductSearchService.class) {
                if (instance == null) {
                    instance = new CustomProductSearchService();
                }
            }
        }
        return instance;
    }

    @Override
    public List<Product> search(SearchCriteria searchCriteria) {
        Predicate<Product> predicate = createPricePredicate(searchCriteria.getMinPrice(), searchCriteria.getMaxPrice())
                .and(createDescriptionPredicate(searchCriteria.getDescription(), searchCriteria.getSearchType()));
        return productDao.findProducts(predicate);
    }

    private Predicate<Product> createPricePredicate(BigDecimal minPrice, BigDecimal maxPrice) {
        return (product) -> (minPrice == null || product.getPrice().compareTo(minPrice) >= 0)
                && (maxPrice == null || product.getPrice().compareTo(maxPrice) <= 0);
    }

    private Predicate<Product> createDescriptionPredicate(String description, SearchType searchType) {
        return (product) -> StringUtils.isBlank(description) || productDescriptionFiltersConfiguration
                .getFilterBySearchType(searchType)
                .test(product, description);
    }
}
