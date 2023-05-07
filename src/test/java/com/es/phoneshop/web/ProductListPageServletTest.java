package com.es.phoneshop.web;

import com.es.phoneshop.exception.ProductNotFoundException;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.sorting.SortField;
import com.es.phoneshop.model.product.sorting.SortOrder;
import com.es.phoneshop.service.ProductService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProductListPageServletTest {

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private ProductService productService;

    private final ProductListPageServlet servlet = new ProductListPageServlet();
    private List<Product> productList;

    @Before
    public void setup() {
        productList = createProductList();
        servlet.setProductService(productService);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
    }

    private List<Product> createProductList() {
        return new ArrayList<>() {{
            add(new Product());
        }};
    }

    @Test
    public void givenNullSortOrderAndField_whenDoGet_thenSetProductsAttribute() throws ServletException, IOException {
        String description = "query";
        when(request.getParameter("query")).thenReturn(description);
        when(productService.findProductsByDescription(description)).thenReturn(productList);

        servlet.doGet(request, response);

        verify(productService).findProductsByDescription(description);
        verify(request).setAttribute("products", productList);
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void givenNullDescription_whenDoGet_thenSetProductsAttribute() throws ServletException, IOException {
        when(productService.findProductsByDescription(null)).thenReturn(productList);

        servlet.doGet(request, response);

        verify(productService).findProductsByDescription(null);
        verify(request).setAttribute("products", productList);
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void givenValidDescriptionAndOrdering_whenDoGet_thenSetSortedProductsToProductsAttribute() throws ServletException, IOException {
        String description = "description";
        String field = SortField.PRICE.toString();
        String order = SortOrder.ASCENDING.toString();
        when(request.getParameter("query")).thenReturn(description);
        when(request.getParameter("field")).thenReturn(field);
        when(request.getParameter("order")).thenReturn(order);
        when(productService.findProductsByDescriptionWithOrdering(description, SortField.PRICE, SortOrder.ASCENDING)).thenReturn(productList);

        servlet.doGet(request, response);

        verify(productService).findProductsByDescriptionWithOrdering(description, SortField.PRICE, SortOrder.ASCENDING);
        verify(request).setAttribute("products", productList);
        verify(requestDispatcher).forward(request, response);
    }

    @Test(expected = ProductNotFoundException.class)
    public void givenNonExistingIdForProductWithPriceHistory_whenDoGet_thenThrowProductNotFoundException() throws ServletException, IOException {
        long id = 1L;
        when(request.getParameter("productToShowPriceHistoryId")).thenReturn(Long.toString(id));
        when(productService.getProduct(id)).thenThrow(new ProductNotFoundException(id));

        servlet.doGet(request, response);
    }

    @Test(expected = NumberFormatException.class)
    public void givenIdThatCanNotBeCastedToNumber_whenDoGet_thenThrowNumberFormatException() throws ServletException, IOException {
        String id = "invalid id";
        when(request.getParameter("productToShowPriceHistoryId")).thenReturn(id);

        servlet.doGet(request, response);
    }

    @Test
    public void givenValidIdForProductWithPriceHistory_whenDoGet_thenSetProductToAttribute() throws ServletException, IOException {
        Product product = new Product();
        product.setId(1L);
        when(request.getParameter("productToShowPriceHistoryId")).thenReturn(product.getId().toString());
        when(productService.getProduct(product.getId())).thenReturn(product);

        servlet.doGet(request, response);

        verify(productService).getProduct(product.getId());
        verify(request).setAttribute("productToShowPriceHistory", product);
        verify(requestDispatcher).forward(request, response);
    }
}