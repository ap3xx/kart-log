package com.ap3x.kartlog.service.impl;

import com.ap3x.kartlog.model.LapLog;
import org.junit.Before;
import org.junit.Test;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

import static com.ap3x.kartlog.config.LogConfig.configLog;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class LapParserImplTest {

    private LapParserImpl processor;

    @Before
    public void setUp() {
        configLog();
        processor = new LapParserImpl();
    }

    @Test
    public void processLogFile(){
        final String logFile = Objects.requireNonNull(
                getClass().getClassLoader().getResource("test_pass.log")).getPath();
        List rows = processor.processLogFile(logFile);

        assertEquals(23, rows.size());
    }

    @Test
    public void processLogFile_badLines(){
        final String logFile = Objects.requireNonNull(
                getClass().getClassLoader().getResource("test_bad_lines.log")).getPath();
        List rows = processor.processLogFile(logFile);

        assertEquals(19, rows.size());
    }

    @Test
    public void processLogFile_noFile() {
        List rows = processor.processLogFile("this_does_not_exist.log");

        assertNull(rows);
    }

    @Test
    public void parseLogRow() {
        final String rawLogRow = "23:49:08.277      038 – F.MASSA                1\t\t1:02.852                        44,275";
        final LapLog lapLog = processor.parseLogRow(rawLogRow);

        assertEquals(LocalTime.parse("23:49:08.277"), lapLog.getLogTime());
        assertEquals(38, lapLog.getPilotCode().intValue());
        assertEquals("F.MASSA", lapLog.getPilotName());
        assertEquals(1, lapLog.getLapNumber().intValue());
        assertEquals(62852L, lapLog.getLapTime().toMillis());
        assertEquals(44.275, lapLog.getLapSpeed(), 1);
    }

    @Test
    public void parseLogRow_badPilotCode() {
        final String rawLogRow = "23:49:08.277      0A8 – F.MASSA                1\t\t1:02.852                        44,275";
        final LapLog lapLog = processor.parseLogRow(rawLogRow);

        assertEquals("FMTERR", lapLog.getPilotName());
    }

    @Test
    public void parseLogRow_badLapSpeed() {
        final String rawLogRow = "23:49:08.277      038 – F.MASSA                1\t\t1:02.852                        44,A75";
        final LapLog lapLog = processor.parseLogRow(rawLogRow);

        assertEquals("FMTERR", lapLog.getPilotName());
    }

    @Test
    public void parseLogRow_badLapNumber() {
        final String rawLogRow = "23:49:08.277      038 – F.MASSA                B\t\t1:02.852                        44,275";
        final LapLog lapLog = processor.parseLogRow(rawLogRow);

        assertEquals("FMTERR", lapLog.getPilotName());
    }

    @Test
    public void parseLogRow_badLapTime() {
        String rawLogRow = "23:49:08.277      038 – F.MASSA                1\t\tA:02.852                        44,275";
        LapLog lapLog = processor.parseLogRow(rawLogRow);
        assertEquals("FMTERR", lapLog.getPilotName());

        rawLogRow = "23:49:08.277      038 – F.MASSA                1\t\t1:AB.852                        44,275";
        lapLog = processor.parseLogRow(rawLogRow);
        assertEquals("FMTERR", lapLog.getPilotName());

        rawLogRow = "23:49:08.277      038 – F.MASSA                1\t\t1:02.ABC                        44,275";
        lapLog = processor.parseLogRow(rawLogRow);
        assertEquals("FMTERR", lapLog.getPilotName());
    }

    @Test
    public void parseLogRow_badLogTime() {
        String rawLogRow = "2019-12-30T23:49:08.277      038 – F.MASSA                1\t\t1:02.852             44,275";
        LapLog lapLog = processor.parseLogRow(rawLogRow);
        assertEquals("FMTERR", lapLog.getPilotName());

        rawLogRow = "A23:49:08      038 – F.MASSA                1\t\t1:02.852                        44,275";
        lapLog = processor.parseLogRow(rawLogRow);
        assertEquals("FMTERR", lapLog.getPilotName());
    }

    @Test
    public void parseLogRow_shortLine() {
        String rawLogRow = "23:49:08.277      038 – F.MASSA";
        LapLog lapLog = processor.parseLogRow(rawLogRow);
        assertEquals("FMTERR", lapLog.getPilotName());
    }

    @Test
    public void getDuration() {
        String durationString = "1:02.852";
        Duration duration = processor.getDuration(durationString);
        assertEquals(62852L, duration.toMillis());

        durationString = "2.852";
        duration = processor.getDuration(durationString);
        assertEquals(2852L, duration.toMillis());

        durationString = "1:02";
        duration = processor.getDuration(durationString);
        assertEquals(62000L, duration.toMillis());
    }

    @Test(expected = NumberFormatException.class)
    public void getDuration_badTime() {
        final String durationString = "1:02.A";
        processor.getDuration(durationString);
    }

    @Test(expected = NumberFormatException.class)
    public void getDuration_badTimeB() {
        final String durationString = "1:BB.123";
        processor.getDuration(durationString);
    }

    @Test(expected = NumberFormatException.class)
    public void getDuration_badTimeC() {
        final String durationString = "C:02.123";
        processor.getDuration(durationString);
    }
}