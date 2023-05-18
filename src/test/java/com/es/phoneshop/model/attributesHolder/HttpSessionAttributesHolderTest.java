package com.es.phoneshop.model.attributesHolder;

import jakarta.servlet.http.HttpSession;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class HttpSessionAttributesHolderTest {

    @Mock
    private HttpSession session;
    private HttpSessionAttributesHolder attributesHolder;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        attributesHolder = new HttpSessionAttributesHolder(session);
    }

    @Test
    public void givenAttributeName_whenGetAttribute_thenReturnAttribute() {
        Integer expectedAttribute = 1;
        String attributeName = "integer";
        when(session.getAttribute(attributeName)).thenReturn(expectedAttribute);

        Integer actual = (Integer) attributesHolder.getAttribute(attributeName);

        assertEquals(expectedAttribute, actual);
    }

    @Test
    public void givenAttribute_whenSetAttribute_thenSetAttributeToSession() {
        String expectedAttribute = "expected attribute";
        String attributeName = "attribute";

        attributesHolder.setAttribute(attributeName, expectedAttribute);

        verify(session).setAttribute(attributeName, expectedAttribute);
    }
}
