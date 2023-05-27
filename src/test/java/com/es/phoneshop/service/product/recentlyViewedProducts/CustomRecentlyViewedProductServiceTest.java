package com.es.phoneshop.service.product.recentlyViewedProducts;

import com.es.phoneshop.model.attributesHolder.AttributesHolder;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.service.product.ProductService;
import com.es.phoneshop.service.product.recentlyViewedProducts.CustomRecentlyViewedProductService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CustomRecentlyViewedProductServiceTest {

    @Mock
    private ProductService productService;
    @Mock
    private AttributesHolder attributesHolder;
    @InjectMocks
    private final CustomRecentlyViewedProductService recentlyViewedProductService = CustomRecentlyViewedProductService.getInstance();

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void givenValidProductsId_whenGetRecentlyViewedProducts_thenReturnProducts() {
        int productsCount = 5;
        List<Product> expectedProducts = IntStream.range(0, productsCount)
                .mapToObj(x -> new Product.ProductBuilder().build())
                .toList();
        List<Long> idList = expectedProducts.stream()
                .mapToLong(Product::getId)
                .boxed()
                .toList();
        when(attributesHolder.getAttribute(any())).thenReturn(idList);
        expectedProducts.forEach(product ->
                when(productService.getProduct(product.getId())).thenReturn(product)
        );

        List<Product> actualProducts = recentlyViewedProductService.getRecentlyViewedProducts(attributesHolder);

        assertEquals(expectedProducts, actualProducts);
    }

    @Test
    public void givenProductId_whenAddRecentlyViewedProduct_thenAddProduct() {
        long id = 2L;
        List<Long> actualProductsId = new ArrayList<>() {{
            add(1L);
        }};
        List<Long> expectedProductsId = new ArrayList<>() {{
            add(id);
        }};
        expectedProductsId.addAll(actualProductsId);
        when(attributesHolder.getAttribute(any())).thenReturn(actualProductsId);

        recentlyViewedProductService.addRecentlyViewedProduct(attributesHolder, id);

        assertEquals(expectedProductsId, actualProductsId);
    }

    @Test
    public void givenProductIdThatAlreadyWasViewed_whenAddRecentlyViewedProduct_thenPutIdToFirstPosition() {
        long id = 2L;
        List<Long> actualProductsId = new ArrayList<>() {{
            add(1L);
            add(id);
        }};
        List<Long> expectedProductsId = new ArrayList<>() {{
            add(id);
            add(1L);
        }};
        when(attributesHolder.getAttribute(any())).thenReturn(actualProductsId);

        recentlyViewedProductService.addRecentlyViewedProduct(attributesHolder, id);

        assertEquals(expectedProductsId, actualProductsId);
    }

    @Test
    public void givenListOfViewedProductsIsFull_whenAddRecentlyViewedProduct_thenRemoveOldestProduct() {
        long id = CustomRecentlyViewedProductService.getMaxViewedProductsCount() + 1;
        List<Long> actualProductsId = createFullListOfViewedProductsId();
        List<Long> expectedProductsId = new ArrayList<>() {{
            add(id);
        }};
        expectedProductsId.addAll(actualProductsId.subList(0, actualProductsId.size() - 1));
        when(attributesHolder.getAttribute(any())).thenReturn(actualProductsId);

        recentlyViewedProductService.addRecentlyViewedProduct(attributesHolder, id);

        assertEquals(expectedProductsId, actualProductsId);
    }

    private List<Long> createFullListOfViewedProductsId() {
        int size = CustomRecentlyViewedProductService.getMaxViewedProductsCount();
        List<Long> immutableList = IntStream.range(1, size + 1)
                .mapToLong(x -> x)
                .boxed()
                .toList();
        return new ArrayList<>(immutableList);
    }

    @Test
    public void givenIdList_whenSetRecentlyViewedProducts_thenSetListToSessionAttribute() {
        List<Long> idList = List.of(1L, 2L, 3L);

        recentlyViewedProductService.setRecentlyViewedProducts(attributesHolder, idList);

        verify(attributesHolder).setAttribute(any(), eq(idList));
    }

}
