package com.es.phoneshop.web.listeners;

import com.es.phoneshop.dao.product.CustomProductDao;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProductDemoDataServletContextListenerTest {

    @Mock
    private ServletContextEvent servletContextEvent;
    @Mock
    private ServletContext servletContext;
    @Mock
    private CustomProductDao customProductDao;
    @Spy
    private final ProductDemoDataServletContextListener productDemoDataServletContextListener = new ProductDemoDataServletContextListener();

    @Test
    public void givenFalseInsertDataParameter_whenContextInitialized_thenDoNotInsertData() {
        try (MockedStatic<CustomProductDao> productDaoMockedStatic = mockStatic(CustomProductDao.class)) {
            productDaoMockedStatic.when(CustomProductDao::getInstance).thenReturn(customProductDao);
            when(servletContextEvent.getServletContext()).thenReturn(servletContext);
            when(servletContext.getInitParameter("insertDemoData")).thenReturn("false");

            productDemoDataServletContextListener.contextInitialized(servletContextEvent);

            verifyNoInteractions(customProductDao);
        }
    }

    @Test
    public void givenTrueInsertDataParameter_whenContextInitialized_thenInsertData() {
        try (MockedStatic<CustomProductDao> productDaoMockedStatic = mockStatic(CustomProductDao.class)) {
            productDaoMockedStatic.when(CustomProductDao::getInstance).thenReturn(customProductDao);
            when(servletContextEvent.getServletContext()).thenReturn(servletContext);
            when(servletContext.getInitParameter("insertDemoData")).thenReturn("true");

            productDemoDataServletContextListener.contextInitialized(servletContextEvent);

            verify(customProductDao, atLeastOnce()).save(any());
        }
    }
}
