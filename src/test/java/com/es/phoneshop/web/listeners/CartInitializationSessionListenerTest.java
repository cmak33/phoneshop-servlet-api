package com.es.phoneshop.web.listeners;

import com.es.phoneshop.service.cart.CustomCartService;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSessionEvent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CartInitializationSessionListenerTest {

    @Mock
    private HttpSessionEvent httpSessionEvent;
    @Mock
    private HttpSession httpSession;
    @Mock
    private ServletContext servletContext;
    @Mock
    private CustomCartService customCartService;
    private final CartInitializationSessionListener sessionListener = new CartInitializationSessionListener();

    @Before
    public void setup() {
        when(httpSessionEvent.getSession()).thenReturn(httpSession);
        when(httpSession.getServletContext()).thenReturn(servletContext);
    }

    @Test
    public void givenFalseInitializeProductsParameter_whenSessionCreated_thenDoNotInitialize() {
        try (MockedStatic<CustomCartService> serviceMockedStatic = mockStatic(CustomCartService.class)) {
            serviceMockedStatic.when(CustomCartService::getInstance).thenReturn(customCartService);
            when(servletContext.getInitParameter("initializeCart")).thenReturn("false");

            sessionListener.sessionCreated(httpSessionEvent);

            verifyNoInteractions(customCartService);
        }
    }

    @Test
    public void givenTrueInitializeProductsParameter_whenSessionCreated_thenInitialize() {
        try (MockedStatic<CustomCartService> serviceMockedStatic = mockStatic(CustomCartService.class)) {
            serviceMockedStatic.when(CustomCartService::getInstance).thenReturn(customCartService);
            when(servletContext.getInitParameter("initializeCart")).thenReturn("true");

            sessionListener.sessionCreated(httpSessionEvent);

            verify(customCartService).setCart(any(), any());
        }
    }
}
