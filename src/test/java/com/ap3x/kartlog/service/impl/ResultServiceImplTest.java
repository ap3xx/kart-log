package com.ap3x.kartlog.service.impl;

import com.ap3x.kartlog.model.LapLog;
import com.ap3x.kartlog.model.RacerResult;
import com.ap3x.kartlog.service.LapParser;
import org.junit.Before;
import org.junit.Test;

import java.time.Duration;
import java.time.LocalTime;
import java.util.*;

import static org.junit.Assert.*;

public class ResultServiceImplTest {

    private ResultServiceImpl resultService;

    @Before
    public void setUp() {
        resultService = new ResultServiceImpl();
    }

    @Test
    public void getRaceFinalGrid() {
        final LapParser processor = new LapParserImpl();
        final String logFile = Objects.requireNonNull(
                getClass().getClassLoader().getResource("test_pass.log")).getPath();
        List<LapLog> rows = processor.processLogFile(logFile);

        List<RacerResult> results = resultService.getRaceFinalGrid(rows);

        assertEquals("F.MASSA", results.get(0).getPilotName());
        assertEquals(4, results.get(0).getLastCompleteLap().intValue());
        assertEquals("K.RAIKKONEN", results.get(1).getPilotName());
        assertEquals("R.BARRICHELLO", results.get(2).getPilotName());
        assertEquals("M.WEBBER", results.get(3).getPilotName());
        assertEquals("F.ALONSO", results.get(4).getPilotName());
        assertEquals("S.VETTEL", results.get(5).getPilotName());
        assertEquals(2, results.get(5).getLastCompleteLap().intValue());
    }

    @Test
    public void getBestLaps() {
        final LapParser processor = new LapParserImpl();
        final String logFile = Objects.requireNonNull(
                getClass().getClassLoader().getResource("test_pass.log")).getPath();
        List<LapLog> rows = processor.processLogFile(logFile);

        List<LapLog> bestLaps = resultService.getBestLaps(rows);

        assertEquals(62769, bestLaps.get(0).getLapTime().toMillis());
        assertEquals(63076, bestLaps.get(1).getLapTime().toMillis());
        assertEquals(63716, bestLaps.get(2).getLapTime().toMillis());
        assertEquals(64216, bestLaps.get(3).getLapTime().toMillis());
        assertEquals(67011, bestLaps.get(4).getLapTime().toMillis());
        assertEquals(97864, bestLaps.get(5).getLapTime().toMillis());
    }

    @Test
    public void populateBestLapMap_firstLapOfPilot() {
        Map<Integer, LapLog> bestLapsByPilot = new HashMap<>();
        Set<Integer> finished = new HashSet<>();
        LapLog log = getLapLog(1,1    , 100L);
        LapLog log2 = getLapLog(2,1    , 100L);
        bestLapsByPilot.put(2, log2);

        resultService.populateBestLapMap(bestLapsByPilot, log, finished);

        LapLog lapLog = bestLapsByPilot.get(1);
        assertTrue(finished.isEmpty());
        assertEquals(LocalTime.MIDNIGHT, lapLog.getLogTime());
        assertEquals(1, lapLog.getPilotCode().intValue());
        assertEquals("test", lapLog.getPilotName());
        assertEquals(1, lapLog.getLapNumber().intValue());
        assertEquals(100L, lapLog.getLapTime().toMillis());
        assertEquals(100f, lapLog.getLapSpeed(), 1);
    }

    @Test
    public void populateBestLapMap_betterLapOfPilot() {
        Map<Integer, LapLog> bestLapsByPilot = new HashMap<>();
        Set<Integer> finished = new HashSet<>();
        LapLog log = getLapLog(1,1    , 100L);
        LapLog log2 = getLapLog(2,1    , 100L);
        LapLog fasterLap = getLapLog(1, 2, 99L);
        bestLapsByPilot.put(1, log);
        bestLapsByPilot.put(2, log2);

        resultService.populateBestLapMap(bestLapsByPilot, fasterLap, finished);

        LapLog lapLog = bestLapsByPilot.get(1);
        assertTrue(finished.isEmpty());
        assertEquals(LocalTime.MIDNIGHT, lapLog.getLogTime());
        assertEquals(1, lapLog.getPilotCode().intValue());
        assertEquals("test", lapLog.getPilotName());
        assertEquals(2, lapLog.getLapNumber().intValue());
        assertEquals(99L, lapLog.getLapTime().toMillis());
        assertEquals(100f, lapLog.getLapSpeed(), 1);
    }

    @Test
    public void populateBestLapMap_firstLap() {
        Map<Integer, LapLog> bestLapsByPilot = new HashMap<>();
        Set<Integer> finished = new HashSet<>();
        LapLog log = getLapLog(1,1    , 100L);

        resultService.populateBestLapMap(bestLapsByPilot, log, finished);

        LapLog lapLog = bestLapsByPilot.get(1);
        assertTrue(finished.isEmpty());
        assertEquals(LocalTime.MIDNIGHT, lapLog.getLogTime());
        assertEquals(1, lapLog.getPilotCode().intValue());
        assertEquals("test", lapLog.getPilotName());
        assertEquals(1, lapLog.getLapNumber().intValue());
        assertEquals(100L, lapLog.getLapTime().toMillis());
        assertEquals(100f, lapLog.getLapSpeed(), 1);
    }

    @Test
    public void populateBestLapMap_lastLap() {
        Map<Integer, LapLog> bestLapsByPilot = new HashMap<>();
        Set<Integer> finished = new HashSet<>();
        LapLog log = getLapLog(1,4  , 100L);

        resultService.populateBestLapMap(bestLapsByPilot, log, finished);

        LapLog lapLog = bestLapsByPilot.get(1);
        assertFalse(finished.isEmpty());
        assertEquals(LocalTime.MIDNIGHT, lapLog.getLogTime());
        assertEquals(1, lapLog.getPilotCode().intValue());
        assertEquals("test", lapLog.getPilotName());
        assertEquals(4, lapLog.getLapNumber().intValue());
        assertEquals(100L, lapLog.getLapTime().toMillis());
        assertEquals(100f, lapLog.getLapSpeed(), 1);
    }

    @Test
    public void populateBestLapMap_afterFirstFinished() {
        Map<Integer, LapLog> bestLapsByPilot = new HashMap<>();
        Set<Integer> finished = new HashSet<>();
        LapLog log = getLapLog(1,4    , 100L);
        LapLog log2 = getLapLog(2,1    , 100L);
        bestLapsByPilot.put(1, log);
        bestLapsByPilot.put(2, log2);
        finished.add(1);

        resultService.populateBestLapMap(bestLapsByPilot, log2, finished);

        assertEquals(2, finished.size());
    }

    @Test
    public void populateResultMap_updateRaceFinished() {
        LapLog log = getLapLog(1,4    , 100L);
        Map<Integer, RacerResult> results = new HashMap<>();
        RacerResult racerResult = new RacerResult(
                1,
                "test",
                100L,
                1,
                100f
        );
        results.put(1, racerResult);

        assertEquals(racerResult, resultService.updateRacerResult(results, log));
        assertEquals(4, results.get(1).getLastCompleteLap().intValue());
        assertEquals(200f, results.get(1).getLapSpeedSum(), 1);
        assertEquals(200L, results.get(1).getRaceTotalTime().longValue());
    }

    @Test
    public void populateResultMap_updateRaceNotFinished() {
        LapLog log = getLapLog(1,2, 100L);
        Map<Integer, RacerResult> results = new HashMap<>();
        results.put(1, new RacerResult(
                1,
                "test",
                100L,
                1,
                100f
        ));

        assertNull(resultService.populateResultMap(results, log));
        assertEquals(2, results.get(1).getLastCompleteLap().intValue());
        assertEquals(200f, results.get(1).getLapSpeedSum(), 1);
        assertEquals(200L, results.get(1).getRaceTotalTime().longValue());
    }

    @Test
    public void populateResultMap_insert() {
        LapLog log = getLapLog(1,3, 100L);
        Map<Integer, RacerResult> results = new HashMap<>();

        assertNull(resultService.populateResultMap(results, log));
        RacerResult inserted = results.get(1);
        assertEquals(1, inserted.getPilotCode().intValue());
        assertEquals("test", inserted.getPilotName());
        assertEquals(100L, inserted.getRaceTotalTime().longValue());
        assertEquals(1, inserted.getLastCompleteLap().intValue());
        assertEquals(100f, inserted.getLapSpeedSum(), 1);
        assertNull(inserted.getTimeAfterFirstRacer());
    }

    @Test
    public void calculateLastLap() {
        LapLog log = getLapLog(2,4, 100L);
        Map<Integer, RacerResult> results = new HashMap<>();
        RacerResult firstRacer = new RacerResult(1,"test",400L,4,100f);
        results.put(1, firstRacer);
        results.put(2, new RacerResult(2,"test2", 400L, 3, 300f));

        resultService.calculateLastLap(results, firstRacer, log);

        RacerResult result = results.get(2);
        assertEquals(2, result.getPilotCode().intValue());
        assertEquals("test2", result.getPilotName());
        assertEquals(500L, result.getRaceTotalTime().longValue());
        assertEquals(4, result.getLastCompleteLap().intValue());
        assertEquals(400f, result.getLapSpeedSum(), 1);
        assertEquals(100L, result.getTimeAfterFirstRacer().toMillis());
    }

    @Test
    public void insertRacerResult() {
        LapLog log = getLapLog(1,3, 100L);
        Map<Integer, RacerResult> results = new HashMap<>();

        resultService.insertRacerResult(results, log);

        RacerResult inserted = results.get(1);
        assertEquals(1, inserted.getPilotCode().intValue());
        assertEquals("test", inserted.getPilotName());
        assertEquals(100L, inserted.getRaceTotalTime().longValue());
        assertEquals(1, inserted.getLastCompleteLap().intValue());
        assertEquals(100f, inserted.getLapSpeedSum(), 1);
        assertNull(inserted.getTimeAfterFirstRacer());
    }

    @Test
    public void updateRacerResult() {
        LapLog log = getLapLog(1,2, 100L);
        Map<Integer, RacerResult> results = new HashMap<>();
        results.put(1, new RacerResult(1,"test",100L,1,100f));

        assertNull(resultService.updateRacerResult(results, log));
        assertEquals(2, results.get(1).getLastCompleteLap().intValue());
        assertEquals(200f, results.get(1).getLapSpeedSum(), 1);
        assertEquals(200L, results.get(1).getRaceTotalTime().longValue());
    }

    @Test
    public void updateRacerResult_finishedRace() {
        LapLog log = getLapLog(1,4, 100L);
        Map<Integer, RacerResult> results = new HashMap<>();
        RacerResult racerResult = new RacerResult(1,"test",300L,3,300f);
        results.put(1, racerResult);

        assertEquals(racerResult, resultService.updateRacerResult(results, log));
        assertEquals(4, results.get(1).getLastCompleteLap().intValue());
        assertEquals(400f, results.get(1).getLapSpeedSum(), 1);
        assertEquals(400L, results.get(1).getRaceTotalTime().longValue());
    }

    @Test
    public void getComparator() {
        Comparator comparator = resultService.getComparator();
        assertNotNull(comparator);
    }

    private LapLog getLapLog(Integer pilotCode, Integer lapNumber, Long lapDuration) {
        return new LapLog(LocalTime.MIDNIGHT, pilotCode, "test", lapNumber, Duration.ofMillis(lapDuration), 100f);
    }

}