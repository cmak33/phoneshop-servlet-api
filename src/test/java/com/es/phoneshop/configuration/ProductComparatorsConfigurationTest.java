package com.es.phoneshop.configuration;

import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.sorting.SortField;
import com.es.phoneshop.model.product.sorting.SortOrder;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Comparator;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ProductComparatorsConfigurationTest {

    @Mock
    Map<SortField, Comparator<Product>> ascendingComparators;
    @InjectMocks
    private ProductComparatorsConfiguration productComparatorsConfiguration = ProductComparatorsConfiguration.getInstance();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void givenAscendingOrderAndDescriptionField_whenGetComparatorByFieldAndOrder_thenReturnComparator(){
        Comparator<Product> comparator = Comparator.comparing(Product::getDescription);
        when(ascendingComparators.get(SortField.DESCRIPTION)).thenReturn(comparator);

        Comparator<Product> actualComparator = productComparatorsConfiguration.getComparatorByFieldAndOrder(SortField.DESCRIPTION, SortOrder.ASCENDING);

        verify(ascendingComparators).get(SortField.DESCRIPTION);
        assertEquals(comparator,actualComparator);
    }

    @Test
    public void givenDescendingOrderAndPriceField_whenGetComparatorByFieldAndOrder_thenReturnComparator(){
        Comparator<Product> comparator = Comparator.comparing(Product::getPrice);
        Comparator<Product> expected = comparator.reversed();
        when(ascendingComparators.get(SortField.PRICE)).thenReturn(comparator);

        Comparator<Product> actualComparator = productComparatorsConfiguration.getComparatorByFieldAndOrder(SortField.PRICE, SortOrder.DESCENDING);

        verify(ascendingComparators).get(SortField.PRICE);
        assertEquals(expected,actualComparator);
    }
}
