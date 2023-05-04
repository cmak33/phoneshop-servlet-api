package com.es.phoneshop.service;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.exception.ProductNotFoundException;
import com.es.phoneshop.model.product.Product;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CustomProductServiceTest {

    @Mock
    private ProductDao productDao;
    @InjectMocks
    private CustomProductService customProductService;

    @Before
    public void setup() {
        customProductService = CustomProductService.getInstance();
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = ProductNotFoundException.class)
    public void givenNullId_whenGetProduct_thenTrowProductNotFoundException() {
        customProductService.getProduct(null);
    }

    @Test
    public void givenValidId_whenGetProduct_thenReturnProduct() {
        Product product = new Product();
        when(productDao.getProduct(product.getId())).thenReturn(Optional.of(product));

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
}
