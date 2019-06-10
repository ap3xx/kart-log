package com.ap3x.kartlog.service.impl;

import com.ap3x.kartlog.model.LapLog;
import com.ap3x.kartlog.model.RacerResult;
import com.ap3x.kartlog.service.ResultService;

import java.time.Duration;
import java.util.*;
import java.util.logging.Logger;

public class ResultServiceImpl implements ResultService {

    private static final Logger LOGGER = Logger.getLogger(ResultServiceImpl.class.getName());

    @Override
    public List<RacerResult> getRaceFinalGrid(final List<LapLog> lapLogs) {
        LOGGER.info("Calculating race results...");

        final Map<Integer, RacerResult> results = new HashMap<>();
        RacerResult firstPosition = null;

        for (LapLog lapLog : lapLogs) {
            if (firstPosition == null)
                firstPosition = populateResultMap(results, lapLog);
            else if (results.get(lapLog.getPilotCode()).getTimeAfterFirstRacer() == null) {
                calculateLastLap(results, firstPosition, lapLog);
            }
            LOGGER.fine("New log row parsed, winner: " + firstPosition);
        }

        final List<RacerResult> racerResults = new ArrayList<>(results.values());
        racerResults.sort(getComparator());

        LOGGER.info("Finished calculating racing results...");
        return racerResults;
    }

    @Override
    public List<LapLog> getBestLaps(final List<LapLog> lapLogs) {
        final Map<Integer, LapLog> bestLapsByPilot = new HashMap<>();
        Set<Integer> finished = new HashSet<>();

        for (LapLog lapLog : lapLogs) {
            if (finished.isEmpty())
                populateBestLapMap(bestLapsByPilot, lapLog, finished);
            else if (!finished.contains(lapLog.getPilotCode()))
                populateBestLapMap(bestLapsByPilot, lapLog, finished);
            LOGGER.fine("New log row parsed, finished: " + finished);
        }

        List<LapLog> bestLaps = new ArrayList<>(bestLapsByPilot.values());
        bestLaps.sort(Comparator.comparing(LapLog::getLapTime));
        return bestLaps;
    }

    void populateBestLapMap(final Map<Integer, LapLog> bestLapsByPilot, final LapLog lapLog,
                               final Set<Integer> finished) {
        if (bestLapsByPilot.containsKey(lapLog.getPilotCode())){
            if (bestLapsByPilot.get(lapLog.getPilotCode()).getLapTime().compareTo(lapLog.getLapTime()) > 0) {
                bestLapsByPilot.put(lapLog.getPilotCode(), lapLog);
            }
            if (lapLog.getLapNumber() == 4 || !finished.isEmpty()) finished.add(lapLog.getPilotCode());
        } else{
            bestLapsByPilot.put(lapLog.getPilotCode(), lapLog);
            if (lapLog.getLapNumber() == 4 || !finished.isEmpty()) finished.add(lapLog.getPilotCode());
        }
    }

    void calculateLastLap(Map<Integer, RacerResult> results, RacerResult firstRacer, LapLog lapLog) {
        LOGGER.fine(String.format("Updating race info for racer: %s", lapLog.getPilotName()));
        final RacerResult result = results.get(lapLog.getPilotCode());

        result.setLastCompleteLap(lapLog.getLapNumber());
        result.setRaceTotalTime(result.getRaceTotalTime() + lapLog.getLapTime().toMillis());
        result.setLapSpeedSum(result.getLapSpeedSum() + lapLog.getLapSpeed());
        result.setTimeAfterFirstRacer(Duration.ofMillis(result.getRaceTotalTime() - firstRacer.getRaceTotalTime()));
        results.put(lapLog.getPilotCode(), result);
    }

    RacerResult populateResultMap(final Map<Integer, RacerResult> results, final LapLog lapLog) {
        if (results.containsKey(lapLog.getPilotCode())) {
            return updateRacerResult(results, lapLog);
        } else {
            insertRacerResult(results, lapLog);
        }
        return null;
    }

    void insertRacerResult(final Map<Integer, RacerResult> results, final LapLog lapLog) {
        LOGGER.fine(String.format("First loop for racer: %s", lapLog.getPilotName()));
        results.put(
                lapLog.getPilotCode(),
                new RacerResult(
                    lapLog.getPilotCode(),
                    lapLog.getPilotName(),
                    lapLog.getLapTime().toMillis(),
                    1,
                    lapLog.getLapSpeed()
                )
        );
    }

    RacerResult updateRacerResult(final Map<Integer, RacerResult> results, final LapLog lapLog) {
        LOGGER.fine(String.format("Updating race info for racer: %s", lapLog.getPilotName()));
        final RacerResult result = results.get(lapLog.getPilotCode());

        result.setLastCompleteLap(lapLog.getLapNumber());
        result.setRaceTotalTime(result.getRaceTotalTime() + lapLog.getLapTime().toMillis());
        result.setLapSpeedSum(result.getLapSpeedSum() + lapLog.getLapSpeed());
        results.put(lapLog.getPilotCode(), result);

        if (lapLog.getLapNumber() == 4) return result;
        return null;
    }

    Comparator<RacerResult> getComparator() {
        return Comparator.comparing(RacerResult::getLastCompleteLap).reversed()
                            .thenComparing(
                                    RacerResult::getTimeAfterFirstRacer,
                                    Comparator.nullsFirst(Comparator.naturalOrder())
                            );
    }
}
