package com.ap3x.kartlog.service;

import com.ap3x.kartlog.model.LapLog;
import com.ap3x.kartlog.model.RacerResult;

import java.util.List;

public interface ResultService {

    List<RacerResult> getRaceFinalGrid(List<LapLog> lapLogs);

    List<LapLog> getBestLaps(List<LapLog> logs);
}
