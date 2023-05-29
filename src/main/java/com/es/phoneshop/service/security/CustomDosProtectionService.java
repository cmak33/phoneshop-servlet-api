package com.es.phoneshop.service.security;

import lombok.Getter;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Getter
public class CustomDosProtectionService implements DosProtectionService {

    private static volatile CustomDosProtectionService instance;
    private final long MAX_REQUESTS_PER_MINUTE = 40;
    private final long REQUESTS_CLEAR_PERIOD = 60 * 1000;
    private final Map<String, AtomicLong> requestsCount;

    private CustomDosProtectionService() {
        requestsCount = new ConcurrentHashMap<>();
        runClearRequestCountTask();
    }

    private void runClearRequestCountTask() {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                clearRequestsCount();
            }
        };
        timer.schedule(task, REQUESTS_CLEAR_PERIOD, REQUESTS_CLEAR_PERIOD);
    }

    public static CustomDosProtectionService getInstance() {
        if (instance == null) {
            synchronized (CustomDosProtectionService.class) {
                if (instance == null) {
                    instance = new CustomDosProtectionService();
                }
            }
        }
        return instance;
    }

    private void clearRequestsCount() {
        requestsCount.clear();
    }

    @Override
    public boolean isUserAllowed(String ip) {
        if (!requestsCount.containsKey(ip)) {
            synchronized (requestsCount) {
                if (!requestsCount.containsKey(ip)) {
                    requestsCount.put(ip, new AtomicLong(0));
                }
            }
        }
        return requestsCount.get(ip).incrementAndGet() <= MAX_REQUESTS_PER_MINUTE;
    }
}
