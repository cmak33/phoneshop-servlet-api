package com.es.phoneshop.service;

import com.es.phoneshop.configuration.ProductComparatorsConfiguration;
import com.es.phoneshop.dao.product.ProductDao;
import com.es.phoneshop.exception.ProductNotFoundException;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.sorting.SortField;
import com.es.phoneshop.model.product.sorting.SortOrder;
import com.es.phoneshop.service.product.CustomProductService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CustomProductServiceTest {

    @Mock
    private ProductDao productDao;
    @Mock
    private ProductComparatorsConfiguration productComparatorsConfiguration;
    @InjectMocks
    private CustomProductService customProductService = CustomProductService.getInstance();

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test(expected = ProductNotFoundException.class)
    public void givenNullId_whenGetProduct_thenTrowProductNotFoundException() {
        customProductService.getProduct(null);
    }

    @Test
    public void givenValidId_whenGetProduct_thenReturnProduct() {
        Product product = new Product();
        when(productDao.getEntity(product.getId())).thenReturn(Optional.of(product));

        Product actual = customProductService.getProduct(product.getId());

        assertEquals(product, actual);
    }

    @Test
    public void givenEmptyList_whenFindProducts_thenReturnList() {
        List<Product> expected = new ArrayList<>();
        when(productDao.findProducts()).thenReturn(expected);

        List<Product> actual = customProductService.findProducts();

        assertEquals(expected, actual);
    }

    @Test
    public void givenProduct_whenSave_thenSaveProduct() {
        Product product = new Product();

        customProductService.save(product);

        verify(productDao).save(product);
    }

    @Test
    public void givenValidId_whenDelete_thenDeleteProduct() {
        long id = 1L;

        customProductService.delete(id);

        verify(productDao).delete(id);
    }

    @Test
    public void givenDescription_whenFindProductsByDescription_thenReturnListOfProducts() {
        List<Product> products = new ArrayList<>() {{
            add(new Product());
        }};
        String description = "description";
        when(productDao.findProductsByDescription(description)).thenReturn(products);

        List<Product> actualProducts = customProductService.findProductsByDescription(description);

        verify(productDao).findProductsByDescription(description);
        assertEquals(products, actualProducts);
    }

    @Test
    public void givenDescriptionAndOrdering_whenFindProductsByDescriptionWithOrdering_thenReturnSortedList() {
        List<Product> products = new ArrayList<>() {{
            add(new Product());
        }};
        Comparator<Product> comparator = Comparator.comparing(Product::getDescription);
        String description = "description";
        SortOrder order = SortOrder.ASCENDING;
        SortField field = SortField.DESCRIPTION;
        when(productComparatorsConfiguration.getComparatorByFieldAndOrder(field, order)).thenReturn(comparator);
        when(productDao.findProductsByDescriptionWithOrdering(description, comparator)).thenReturn(products);

        List<Product> actualProducts = customProductService.findProductsByDescriptionWithOrdering(description, field, order);

        verify(productComparatorsConfiguration).getComparatorByFieldAndOrder(field, order);
        verify(productDao).findProductsByDescriptionWithOrdering(description, comparator);
        assertEquals(products, actualProducts);
    }

    @Test
    public void givenOrdering_whenFindSortedProducts_thenReturnSortedProducts() {
        List<Product> products = new ArrayList<>() {{
            add(new Product());
        }};
        Comparator<Product> comparator = Comparator.comparing(Product::getDescription);
        SortOrder order = SortOrder.ASCENDING;
        SortField field = SortField.DESCRIPTION;
        when(productComparatorsConfiguration.getComparatorByFieldAndOrder(field, order)).thenReturn(comparator);
        when(productDao.findSortedProducts(comparator)).thenReturn(products);

        List<Product> actualProducts = customProductService.findSortedProducts(field, order);

        verify(productComparatorsConfiguration).getComparatorByFieldAndOrder(field, order);
        verify(productDao).findSortedProducts(comparator);
        assertEquals(products, actualProducts);
    }
}
