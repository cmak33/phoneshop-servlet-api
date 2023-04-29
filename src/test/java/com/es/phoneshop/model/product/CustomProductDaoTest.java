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
        for (int i = 0; i < count; i++) {
            Product product = new Product();
            product.setPrice(price);
            product.setStock(stock);
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
        productDao.clear();
    }

    @Test
    public void testFindProductsNoResults() {
        assertTrue(productDao.findProducts().isEmpty());
    }

    @Test
    public void getProductWhenProductIsNotPresentReturnsNull() {
        assertTrue(productDao.getProduct(0L).isEmpty());
    }

    @Test
    public void getProductWhenProductIsPresentReturnsProduct() {
        Product product = new Product();
        productDao.save(product);

        Optional<Product> actualProduct = productDao.getProduct(product.getId());

        assertTrue(actualProduct.isPresent());
        assertEquals(product, actualProduct.get());
    }

    @Test
    public void findProductsWhenAllProductsAreValidReturnsAll() {
        int productsCount = 10;
        List<Product> validProducts = createValidProducts(productsCount);
        validProducts.forEach(productDao::save);

        List<Product> actualProducts = productDao.findProducts();

        assertEquals(validProducts, actualProducts);
    }

    @Test
    public void findProductsWhenInvalidProductsArePresentReturnsValidProducts() {
        int validProductsCount = 10;
        List<Product> validProducts = createValidProducts(validProductsCount);
        validProducts.forEach(productDao::save);
        int invalidProductsCount = 10;
        List<Product> invalidProducts = createInvalidProducts(invalidProductsCount);
        invalidProducts.forEach(productDao::save);

        List<Product> actualProducts = productDao.findProducts();

        assertEquals(validProducts, actualProducts);
    }

    @Test
    public void saveWhenProductIdIsNullSavesProduct() {
        Product product = new Product();
        productDao.save(product);

        Optional<Product> actualProduct = productDao.getProduct(product.getId());

        assertTrue(actualProduct.isPresent());
        assertEquals(product, actualProduct.get());
    }

    @Test
    public void deleteWhenProductIsNotPresentDoesNotChangeProductList() {
        long id = 1;
        List<Product> productList = createValidProducts(1);
        Product product = productList.get(0);
        product.setId(id);
        productDao.save(product);
        productDao.delete(id + 1);

        List<Product> actualProductList = productDao.findProducts();

        assertEquals(productList, actualProductList);
    }

    @Test
    public void deleteWhenProductIsPresentRemovesProduct() {
        int count = 10;
        List<Product> productList = createValidProducts(count);
        productList.forEach(productDao::save);
        Product productToRemove = productList.get(0);
        productList.remove(productToRemove);
        productDao.delete(productToRemove.getId());

        List<Product> actualProductList = productDao.findProducts();

        assertEquals(productList, actualProductList);
    }
}
