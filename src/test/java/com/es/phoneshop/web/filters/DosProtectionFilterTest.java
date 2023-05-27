package com.es.phoneshop.web.filters;

import com.es.phoneshop.service.security.DosProtectionService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DosProtectionFilterTest {

    @Mock
    private DosProtectionService dosProtectionService;
    @Mock
    private ServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private FilterChain filterChain;
    @InjectMocks
    private final DosProtectionFilter dosProtectionFilter = new DosProtectionFilter();
    private final String IP = "ip";

    @Before
    public void setup() {
        when(request.getRemoteAddr()).thenReturn(IP);
    }

    @Test
    public void givenUserIsAllowed_whenDoFilter_thenDoFilter() throws ServletException, IOException {
        when(dosProtectionService.isUserAllowed(IP)).thenReturn(true);

        dosProtectionFilter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }

    @Test
    public void givenUserIsNotAllowed_whenDoFilter_thenSendTooManyRequestsError() throws ServletException, IOException {
        int tooManyRequestErrorCode = 429;
        when(dosProtectionService.isUserAllowed(IP)).thenReturn(false);

        dosProtectionFilter.doFilter(request, response, filterChain);

        verify(response).sendError(tooManyRequestErrorCode);
    }
}
