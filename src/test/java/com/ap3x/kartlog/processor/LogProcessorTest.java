package com.ap3x.kartlog.processor;

import com.ap3x.kartlog.model.LogRow;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

import static com.ap3x.kartlog.config.LogConfig.configLog;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class LogProcessorTest {

    private LogProcessor processor;

    @Before
    public void setUp() {
        configLog();
        processor = new LogProcessor();
    }

    @Test
    public void processLogFile(){
        final String logFile = Objects.requireNonNull(
                getClass().getClassLoader().getResource("test_pass.log")).getPath();
        List<LogRow> rows = processor.processLogFile(logFile);

        assertEquals(23, rows.size());
    }

    @Test
    public void processLogFile_badLines(){
        final String logFile = Objects.requireNonNull(
                getClass().getClassLoader().getResource("test_bad_lines.log")).getPath();
        List<LogRow> rows = processor.processLogFile(logFile);

        assertEquals(19, rows.size());
    }

    @Test
    public void processLogFile_noFile() {
        List<LogRow> rows = processor.processLogFile("this_does_not_exist.log");

        assertNull(rows);
    }

    @Test
    public void parseLogRow() {
        final String rawLogRow = "23:49:08.277      038 – F.MASSA                1\t\t1:02.852                        44,275";
        final LogRow logRow = processor.parseLogRow(rawLogRow);

        assertEquals(LocalTime.parse("23:49:08.277"), logRow.getLogTime());
        assertEquals(38, logRow.getPilotCode().intValue());
        assertEquals("F.MASSA", logRow.getPilotName());
        assertEquals(1, logRow.getLapNumber().intValue());
        assertEquals(62852L, logRow.getLapTime().toMillis());
        assertEquals(44.275, logRow.getLapSpeed(), 1);
    }

    @Test
    public void parseLogRow_badPilotCode() {
        final String rawLogRow = "23:49:08.277      0A8 – F.MASSA                1\t\t1:02.852                        44,275";
        final LogRow logRow = processor.parseLogRow(rawLogRow);

        assertEquals("FMTERR", logRow.getPilotName());
    }

    @Test
    public void parseLogRow_badLapSpeed() {
        final String rawLogRow = "23:49:08.277      038 – F.MASSA                1\t\t1:02.852                        44,A75";
        final LogRow logRow = processor.parseLogRow(rawLogRow);

        assertEquals("FMTERR", logRow.getPilotName());
    }

    @Test
    public void parseLogRow_badLapNumber() {
        final String rawLogRow = "23:49:08.277      038 – F.MASSA                B\t\t1:02.852                        44,275";
        final LogRow logRow = processor.parseLogRow(rawLogRow);

        assertEquals("FMTERR", logRow.getPilotName());
    }

    @Test
    public void parseLogRow_badLapTime() {
        String rawLogRow = "23:49:08.277      038 – F.MASSA                1\t\tA:02.852                        44,275";
        LogRow logRow = processor.parseLogRow(rawLogRow);
        assertEquals("FMTERR", logRow.getPilotName());

        rawLogRow = "23:49:08.277      038 – F.MASSA                1\t\t1:AB.852                        44,275";
        logRow = processor.parseLogRow(rawLogRow);
        assertEquals("FMTERR", logRow.getPilotName());

        rawLogRow = "23:49:08.277      038 – F.MASSA                1\t\t1:02.ABC                        44,275";
        logRow = processor.parseLogRow(rawLogRow);
        assertEquals("FMTERR", logRow.getPilotName());
    }

    @Test
    public void parseLogRow_badLogTime() {
        String rawLogRow = "2019-12-30T23:49:08.277      038 – F.MASSA                1\t\t1:02.852             44,275";
        LogRow logRow = processor.parseLogRow(rawLogRow);
        assertEquals("FMTERR", logRow.getPilotName());

        rawLogRow = "A23:49:08      038 – F.MASSA                1\t\t1:02.852                        44,275";
        logRow = processor.parseLogRow(rawLogRow);
        assertEquals("FMTERR", logRow.getPilotName());
    }

    @Test
    public void parseLogRow_shortLine() {
        String rawLogRow = "23:49:08.277      038 – F.MASSA";
        LogRow logRow = processor.parseLogRow(rawLogRow);
        assertEquals("FMTERR", logRow.getPilotName());
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