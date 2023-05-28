package com.es.phoneshop.dao;

import com.es.phoneshop.dao.product.CustomProductDao;
import com.es.phoneshop.exception.notFoundException.ProductNotFoundException;
import com.es.phoneshop.model.product.Product;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

public class CustomProductDaoTest {

    private final CustomProductDao productDao = CustomProductDao.getInstance();
    private final int MOCKED_PRODUCTS_COUNT = 3;
    private final List<Product> mockedProductsList = IntStream.range(0, MOCKED_PRODUCTS_COUNT)
            .mapToObj(num -> Mockito.mock(Product.class))
            .toList();

    private List<Product> createValidProducts() {
        List<Product> productList = new ArrayList<>(mockedProductsList);
        setValuesToMockedProduct(productList.get(0), 1L, 1, BigDecimal.ONE);
        setValuesToMockedProduct(productList.get(1), 2L, 33, BigDecimal.valueOf(2));
        setValuesToMockedProduct(productList.get(2), 3L, 44, BigDecimal.TEN);
        return productList;
    }

    private void setValuesToMockedProduct(Product product, long id, int stock, BigDecimal price) {
        when(product.getId()).thenReturn(id);
        when(product.getStock()).thenReturn(stock);
        when(product.getPrice()).thenReturn(price);
    }

    private List<Product> createInvalidProducts(int count) {
        return IntStream.range(0, count)
                .mapToObj(num -> new Product())
                .toList();
    }

    @Before
    public void setup() {
        productDao.setEntityList(createValidProducts());
    }

    @Test
    public void givenIdOfNotExistingProduct_whenGetEntity_thenReturnEmptyOptional() {
        assertTrue(productDao.getEntity(-1L).isEmpty());
    }

    @Test
    public void givenNullId_whenGetEntity_thenReturnEmptyOptional() {
        assertTrue(productDao.getEntity(null).isEmpty());
    }

    @Test
    public void givenExistingProduct_whenGetEntity_thenReturnProduct() {
        Product product = productDao.getEntityList().get(0);

        Optional<Product> actualProduct = productDao.getEntity(product.getId());

        assertTrue(actualProduct.isPresent());
        assertEquals(product, actualProduct.get());
    }

    @Test
    public void givenValidProducts_whenFindProducts_thenReturnAllProducts() {
        List<Product> expectedProducts = productDao.getEntityList();

        List<Product> actualProducts = productDao.findProducts();

        assertEquals(expectedProducts, actualProducts);
    }

    @Test
    public void givenValidAndInvalidProducts_whenFindProducts_thenReturnValidProducts() {
        List<Product> validProducts = new ArrayList<>(productDao.getEntityList());
        int invalidProductsCount = 10;
        List<Product> invalidProducts = createInvalidProducts(invalidProductsCount);
        productDao.getEntityList().addAll(invalidProducts);

        List<Product> actualProducts = productDao.findProducts();

        assertEquals(validProducts, actualProducts);
    }

    @Test
    public void givenProductWithNullId_whenSave_thenSaveProduct() {
        Product product = new Product();

        productDao.save(product);

        assertTrue(productDao.getEntityList().contains(product));
    }

    @Test
    public void givenProductWithNotUniqueId_whenSave_thenReplaceOldProduct() {
        int oldProductIndex = 0;
        List<Product> productList = new ArrayList<>(productDao.getEntityList());
        Product productToSave = new Product();
        productToSave.setId(productList.get(oldProductIndex).getId());
        productList.set(oldProductIndex, productToSave);

        productDao.save(productToSave);

        assertEquals(productList, productDao.getEntityList());
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenNullProduct_whenSave_thenThrowIllegalArgumentException() {
        productDao.save(null);
    }

    @Test(expected = ProductNotFoundException.class)
    public void givenIdOfNotExistingProduct_whenDelete_thenThrowProductNotFoundException() {
        productDao.delete(-1L);
    }

    @Test
    public void givenIdOfExistingProduct_whenDelete_thenRemoveProduct() {
        List<Product> expectedProductList = new ArrayList<>(productDao.getEntityList());
        Product productToDelete = expectedProductList.get(0);
        expectedProductList.remove(productToDelete);

        productDao.delete(productToDelete.getId());

        assertEquals(expectedProductList, productDao.getEntityList());
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenNullId_whenDelete_thenThrowIllegalArgumentException() {
        productDao.delete(null);
    }

    @Test
    public void givenValidDescription_whenFindProductsByDescription_thenReturnMatchingProducts() {
        List<Product> expectedProducts = mockedProductsList.subList(0, 2);
        String description = "description";
        when(mockedProductsList.get(0).getDescription()).thenReturn(description);
        when(mockedProductsList.get(1).getDescription()).thenReturn(description);
        when(mockedProductsList.get(2).getDescription()).thenReturn("");

        List<Product> actualProducts = productDao.findProductsByDescription(description);

        assertEquals(expectedProducts, actualProducts);
    }

    @Test
    public void givenValidDescription_whenFindProductsByDescription_thenReturnSortedMatchingProducts() {
        List<Product> expectedProducts = new ArrayList<>() {{
            add(mockedProductsList.get(2));
            add(mockedProductsList.get(0));
        }};
        String description = "three words description";
        when(mockedProductsList.get(0).getDescription()).thenReturn("three");
        when(mockedProductsList.get(1).getDescription()).thenReturn("does not match");
        when(mockedProductsList.get(2).getDescription()).thenReturn("three words");

        List<Product> actualProducts = productDao.findProductsByDescription(description);

        assertEquals(expectedProducts, actualProducts);
    }

    @Test
    public void givenUppercaseDescription_whenFindProductsByDescription_thenReturnSortedMatchingProducts() {
        List<Product> expectedProducts = new ArrayList<>() {{
            add(mockedProductsList.get(1));
            add(mockedProductsList.get(0));
        }};
        String description = "UPPERCASE DESCRIPTION";
        when(mockedProductsList.get(0).getDescription()).thenReturn("upperCase");
        when(mockedProductsList.get(1).getDescription()).thenReturn("uppercase Description");
        when(mockedProductsList.get(2).getDescription()).thenReturn("DOES NOT MATCH");

        List<Product> actualProducts = productDao.findProductsByDescription(description);

        assertEquals(expectedProducts, actualProducts);
    }

    @Test
    public void givenValidDescriptionAndOrdering_whenFindProductsByDescriptionWithOrdering_thenReturnSortedProducts() {
        List<Product> expectedProducts = new ArrayList<>() {{
            add(mockedProductsList.get(2));
            add(mockedProductsList.get(0));
        }};
        String description = "three words description";
        when(mockedProductsList.get(0).getDescription()).thenReturn("three words");
        when(mockedProductsList.get(1).getDescription()).thenReturn("does not match");
        when(mockedProductsList.get(2).getDescription()).thenReturn("three");

        List<Product> actualProducts = productDao.findProductsByDescriptionWithOrdering(description, Comparator.comparing(Product::getPrice).reversed());

        assertEquals(expectedProducts, actualProducts);
    }

    @Test
    public void givenValidComparator_whenFindSortedProducts_thenReturnSortedProducts() {
        List<Product> expectedProducts = new ArrayList<>() {{
            add(mockedProductsList.get(2));
            add(mockedProductsList.get(1));
            add(mockedProductsList.get(0));
        }};
        Comparator<Product> comparator = Comparator.comparing(Product::getPrice);
        when(mockedProductsList.get(0).getPrice()).thenReturn(BigDecimal.valueOf(100));
        when(mockedProductsList.get(1).getPrice()).thenReturn(BigDecimal.valueOf(50));
        when(mockedProductsList.get(2).getPrice()).thenReturn(BigDecimal.valueOf(10));

        List<Product> actualProducts = productDao.findSortedProducts(comparator);

        assertEquals(expectedProducts, actualProducts);
    }
}
