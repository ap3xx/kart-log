package com.ap3x.kartlog.service.impl;

import com.ap3x.kartlog.service.PrintService;
import org.junit.Before;
import org.junit.Test;

import java.time.Duration;

import static org.junit.Assert.*;

public class PrintServiceImplTest {

    PrintServiceImpl service;

    @Before
    public void setUp() {
        service = new PrintServiceImpl();
    }

    @Test
    public void getDurationString() {
        long milis = 1001;

        assertEquals("0:00:01.001", service.getDurationString(milis));
    }

    @Test
    public void getDurationString_duration() {
        Duration duration = Duration.ofMillis(1002);

        assertEquals("0:00:01.002", service.getDurationString(duration));
    }

    @Test
    public void getDurationDifferenceString() {
        Duration duration = Duration.ofMillis(1003);

        assertEquals("+00:01.003", service.getDurationDifferenceString(duration));

    }

    @Test
    public void getDurationDifferenceString_zero() {
        assertEquals("+00:00.000", service.getDurationDifferenceString(null));
    }
}