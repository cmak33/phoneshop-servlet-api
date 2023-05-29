package com.es.phoneshop.service.product;

import com.es.phoneshop.configuration.ProductComparatorsConfiguration;
import com.es.phoneshop.dao.product.ProductDao;
import com.es.phoneshop.exception.NegativeStockException;
import com.es.phoneshop.exception.notFoundException.ProductNotFoundException;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.sorting.SortField;
import com.es.phoneshop.model.product.sorting.SortOrder;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CustomProductServiceTest {

    @Mock
    private ProductDao productDao;
    @Mock
    private ProductComparatorsConfiguration productComparatorsConfiguration;
    @InjectMocks
    @Spy
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

    @Test
    public void givenPositiveChangeInStock_whenChangeStock_thenIncreaseStock() {
        int changeInStock = 10;
        int initialStock = 90;
        int expectedStock = changeInStock + initialStock;
        Long id = 1L;
        Product product = new Product.ProductBuilder().setStock(initialStock).build();
        product.setId(id);
        doReturn(product).when(customProductService).getProduct(id);

        customProductService.changeStock(id, changeInStock);

        verify(productDao).updateStock(product, expectedStock);
    }

    @Test
    public void givenNegativeChangeInStock_whenChangeStock_thenDecreaseStock() {
        int changeInStock = -10;
        int initialStock = 100;
        int expectedStock = changeInStock + initialStock;
        Long id = 1L;
        Product product = new Product.ProductBuilder().setStock(initialStock).build();
        product.setId(id);
        doReturn(product).when(customProductService).getProduct(id);

        customProductService.changeStock(id, changeInStock);

        verify(productDao).updateStock(product, expectedStock);
    }

    @Test(expected = ProductNotFoundException.class)
    public void givenNonExistingProductId_whenChangeStock_thenThrowProductNotFoundException() {
        int changeInStock = -10;
        Long id = 1L;
        doThrow(new ProductNotFoundException(id)).when(customProductService).getProduct(id);

        customProductService.changeStock(id, changeInStock);
    }

    @Test(expected = NegativeStockException.class)
    public void givenNewStockIsNegative_whenChangeStock_thenThrowNegativeStockException() {
        int changeInStock = -1000;
        int initialStock = 100;
        Long id = 1L;
        Product product = new Product.ProductBuilder().setStock(initialStock).build();
        product.setId(id);
        doReturn(product).when(customProductService).getProduct(id);

        customProductService.changeStock(id, changeInStock);
    }
}
