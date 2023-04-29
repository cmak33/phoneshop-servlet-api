package com.es.phoneshop.model.product;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

public class CustomProductDaoTest {
    private final CustomProductDao productDao = CustomProductDao.getInstance();

    private List<Product> createValidProducts(int count) {
        List<Product> productList = new ArrayList<>();
        BigDecimal price = new BigDecimal(1);
        int stock = 1;
        for (int i = 1; i <= count; i++) {
            Product product = new Product();
            product.setPrice(price);
            product.setStock(stock);
            product.setId((long) i);
            productList.add(product);
        }
        return productList;
    }

    private List<Product> createInvalidProducts(int count) {
        return IntStream.range(0, count)
                .mapToObj(num -> new Product())
                .toList();
    }

    @Before
    public void setup() {
        int validProductsCount = 10;
        productDao.setProductList(createValidProducts(validProductsCount));
    }

    @Test
    public void getProductWhenProductIsNotPresentReturnsEmptyOptional() {
        assertTrue(productDao.getProduct(-1L).isEmpty());
    }

    @Test
    public void getProductWhenIdIsNullReturnsEmptyOptional() {
        assertTrue(productDao.getProduct(null).isEmpty());
    }

    @Test
    public void getProductWhenProductIsPresentReturnsProduct() {
        Product product = productDao.getProductList().get(0);

        Optional<Product> actualProduct = productDao.getProduct(product.getId());

        assertTrue(actualProduct.isPresent());
        assertEquals(product, actualProduct.get());
    }

    @Test
    public void findProductsWhenAllProductsAreValidReturnsAll() {
        List<Product> expectedProducts = productDao.getProductList();

        List<Product> actualProducts = productDao.findProducts();

        assertEquals(expectedProducts, actualProducts);
    }

    @Test
    public void findProductsWhenInvalidProductsArePresentReturnsValidProducts() {
        List<Product> validProducts = new ArrayList<>(productDao.getProductList());
        int invalidProductsCount = 10;
        List<Product> invalidProducts = createInvalidProducts(invalidProductsCount);
        productDao.getProductList().addAll(invalidProducts);

        List<Product> actualProducts = productDao.findProducts();

        assertEquals(validProducts, actualProducts);
    }

    @Test
    public void saveWhenProductIdIsNullSavesProduct() {
        Product product = new Product();

        productDao.save(product);

        assertTrue(productDao.getProductList().contains(product));
    }

    @Test
    public void deleteWhenProductIsNotPresentDoesNotChangeProductList() {
        List<Product> expectedProductList = new ArrayList<>(productDao.getProductList());

        productDao.delete(-1L);

        assertEquals(expectedProductList, productDao.getProductList());
    }

    @Test
    public void deleteWhenProductIsPresentRemovesProduct() {
        List<Product> expectedProductList = new ArrayList<>(productDao.getProductList());
        Product productToDelete = expectedProductList.get(0);
        expectedProductList.remove(productToDelete);

        productDao.delete(productToDelete.getId());

        assertEquals(expectedProductList, productDao.getProductList());
    }
}
