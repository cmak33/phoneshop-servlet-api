package com.es.phoneshop.web;

import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.sorting.SortField;
import com.es.phoneshop.model.product.sorting.SortOrder;
import com.es.phoneshop.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProductSortServletTest {

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private ProductService productService;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private PrintWriter writer;
    @InjectMocks
    private final ProductSortServlet servlet = new ProductSortServlet();
    private List<Product> productList;

    @Before
    public void setup() {
        productList = createProductList();
    }

    private List<Product> createProductList() {
        return new ArrayList<>() {{
            add(new Product());
            add(new Product());
        }};
    }

    @Test
    public void givenValidDescriptionAndOrdering_whenDoGet_thenWriteSortedProductsToResponse() throws IOException {
        String description = "description";
        String field = SortField.PRICE.toString();
        String order = SortOrder.ASCENDING.toString();
        String json = "json";
        when(request.getParameter("query")).thenReturn(description);
        when(request.getParameter("field")).thenReturn(field);
        when(request.getParameter("order")).thenReturn(order);
        when(productService.findProductsByDescriptionWithOrdering(description, SortField.PRICE, SortOrder.ASCENDING)).thenReturn(productList);
        when(response.getWriter()).thenReturn(writer);
        when(objectMapper.writeValueAsString(any())).thenReturn(json);

        servlet.doGet(request, response);

        verify(productService).findProductsByDescriptionWithOrdering(description, SortField.PRICE, SortOrder.ASCENDING);
        verify(response).getWriter();
        verify(writer).write(json);
    }

    @Test
    public void givenInvalidOrdering_whenDoGet_thenWriteAllFoundProductsToResponse() throws IOException {
        String description = "description";
        String field = "invalid field";
        String order = "invalid order";
        String json = "json";
        when(request.getParameter("query")).thenReturn(description);
        when(request.getParameter("field")).thenReturn(field);
        when(request.getParameter("order")).thenReturn(order);
        when(productService.findProductsByDescription(description)).thenReturn(productList);
        when(response.getWriter()).thenReturn(writer);
        when(objectMapper.writeValueAsString(any())).thenReturn(json);

        servlet.doGet(request, response);

        verify(productService).findProductsByDescription(description);
        verify(response).getWriter();
        verify(writer).write(json);
    }
}
