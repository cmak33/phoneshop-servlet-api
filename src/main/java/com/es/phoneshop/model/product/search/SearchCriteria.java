package com.es.phoneshop.model.product.search;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class SearchCriteria {

    private String description;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private SearchType searchType;
}
