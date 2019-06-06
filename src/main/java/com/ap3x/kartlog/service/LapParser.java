package com.ap3x.kartlog.service;

import com.ap3x.kartlog.model.LapLog;

import java.util.List;

public interface LapParser {

    List<LapLog> processLogFile(final String logFile);
}
