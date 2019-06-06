package com.ap3x.kartlog.service.impl;

import com.ap3x.kartlog.model.LapLog;
import com.ap3x.kartlog.model.RacerResult;
import com.ap3x.kartlog.service.ResultService;

import java.util.*;
import java.util.logging.Logger;

public class ResultServiceImpl implements ResultService {

    private static final Logger LOGGER = Logger.getLogger(ResultServiceImpl.class.getName());

    @Override
    public List<RacerResult> getRaceResult(List<LapLog> lapLogs) {
        LOGGER.info("Calculating race results...");

        boolean finished = false;
        Map<Integer, RacerResult> results = new HashMap<>();

        for (LapLog lapLog : lapLogs) {
            finished = populateResultMap(finished, results, lapLog);
            LOGGER.fine("New log row parsed, finished: " + finished);
        }

        List<RacerResult> racerResults = new ArrayList<>(results.values());
        racerResults.sort(getComparator());

        LOGGER.info("Finished calculating racing results...");
        return racerResults;
    }

    boolean populateResultMap(boolean finished, Map<Integer, RacerResult> results, LapLog lapLog) {
        if (results.containsKey(lapLog.getPilotCode())) {
            finished = updateRacerResult(finished, results, lapLog);
        } else {
            insertRacerResult(results, lapLog);
        }
        return finished;
    }

    void insertRacerResult(Map<Integer, RacerResult> results, LapLog lapLog) {
        LOGGER.fine(String.format("First loop for racer: %s", lapLog.getPilotName()));
        RacerResult result = new RacerResult();
        result.setPilotCode(lapLog.getPilotCode());
        result.setPilotName(lapLog.getPilotName());
        result.setRaceTotalTime(lapLog.getLapTime().toMillis());
        result.setLastCompleteLap(1);
        results.put(lapLog.getPilotCode(), result);
    }

    boolean updateRacerResult(boolean finished, Map<Integer, RacerResult> results, LapLog lapLog) {
        LOGGER.fine(String.format("Updating race info for racer: %s", lapLog.getPilotName()));
        RacerResult result = results.get(lapLog.getPilotCode());
        if (!finished) {
            result.setLastCompleteLap(lapLog.getLapNumber());
            result.setRaceTotalTime(result.getRaceTotalTime() + lapLog.getLapTime().toMillis());
            if (lapLog.getLapNumber() == 4) finished = true;
        }
        results.put(lapLog.getPilotCode(), result);
        return finished;
    }

    Comparator<RacerResult> getComparator() {
        Comparator<RacerResult> comparator = Comparator.comparing(RacerResult::getLastCompleteLap).reversed();
        return comparator.thenComparing(RacerResult::getRaceTotalTime);
    }
}
