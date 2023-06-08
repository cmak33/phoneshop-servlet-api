package com.es.phoneshop.web.servlets;

import com.es.phoneshop.model.product.search.SearchCriteria;
import com.es.phoneshop.model.product.search.SearchType;
import com.es.phoneshop.service.product.search.CustomProductSearchService;
import com.es.phoneshop.service.product.search.ProductSearchService;
import com.es.phoneshop.validator.parameterValidator.SearchPriceValidator;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.EnumUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdvancedSearchPageServlet extends HttpServlet {

    private ProductSearchService productSearchService;
    private SearchPriceValidator searchPriceValidator;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productSearchService = CustomProductSearchService.getInstance();
        searchPriceValidator = SearchPriceValidator.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String description = request.getParameter("description");
        String minPrice = request.getParameter("minPrice");
        String maxPrice = request.getParameter("maxPrice");
        String searchTypeParameter = request.getParameter("searchType");
        Map<String, String> errors = new HashMap<>();
        searchPriceValidator.validate(minPrice, errors, "minPrice");
        searchPriceValidator.validate(maxPrice, errors, "maxPrice");
        SearchType searchType = EnumUtils.getEnumIgnoreCase(SearchType.class, searchTypeParameter);
        if (searchType == null) {
            errors.put("searchType", "Invalid search type");
        }
        if (errors.isEmpty()) {
            SearchCriteria searchCriteria = new SearchCriteria();
            searchCriteria.setDescription(description);
            if (!maxPrice.isEmpty()) {
                searchCriteria.setMaxPrice(new BigDecimal(maxPrice));
            }
            if (!minPrice.isEmpty()) {
                searchCriteria.setMinPrice(new BigDecimal(minPrice));
            }
            searchCriteria.setSearchType(searchType);
            request.setAttribute("products", productSearchService.search(searchCriteria));
        } else {
            request.setAttribute("errors", errors);
            request.setAttribute("products", new ArrayList<>());
        }
        request.getRequestDispatcher("/WEB-INF/pages/advancedSearch.jsp").forward(request, response);
    }
}
