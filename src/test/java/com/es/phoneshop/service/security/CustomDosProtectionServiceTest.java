package com.es.phoneshop.service.security;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicLong;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CustomDosProtectionServiceTest {

    private final CustomDosProtectionService dosProtectionService = CustomDosProtectionService.getInstance();

    @Before
    public void setup() {
        dosProtectionService.getRequestsCount().clear();
    }

    @Test
    public void givenFirstRequest_whenIsAllowed_thenReturnTrue() {
        String ip = "ip";

        boolean actual = dosProtectionService.isUserAllowed(ip);

        assertTrue(actual);
        assertEquals(1, dosProtectionService.getRequestsCount().size());
        assertEquals(1, dosProtectionService.getRequestsCount().get(ip).intValue());
    }

    @Test
    public void givenMoreThanAllowedRequests_whenIsAllowed_thenReturnFalse() {
        String ip = "ip";
        AtomicLong requestsCount = new AtomicLong(dosProtectionService.getMAX_REQUESTS_PER_MINUTE());
        dosProtectionService.getRequestsCount().put(ip, requestsCount);

        boolean actual = dosProtectionService.isUserAllowed(ip);

        assertFalse(actual);
    }
}
