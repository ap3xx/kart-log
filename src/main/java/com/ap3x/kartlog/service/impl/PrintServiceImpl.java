package com.ap3x.kartlog.service.impl;

import com.ap3x.kartlog.model.RacerResult;
import com.ap3x.kartlog.service.PrintService;

import java.time.Duration;
import java.util.List;
import java.util.logging.Logger;

public class PrintServiceImpl implements PrintService {

    private static final Logger LOGGER = Logger.getLogger(PrintServiceImpl.class.getName());

    @Override
    public void printGrid(List<RacerResult> grid) {
        System.out.println("\n|                           RACE RESULTS                            |\n");

        System.out.println(String.format("| POSITION | %24s | COMPLETED LAPS | TOTAL TIME |\n", "PILOT"));

        for (RacerResult racer : grid) {
            LOGGER.fine("Calculating race results for ");
            System.out.println(
                    String.format("| %8d | %24s | %14d | %10s |",
                            grid.indexOf(racer) + 1,
                            racer.getPilot(),
                            racer.getLastCompleteLap(),
                            getDurationString(racer.getRaceTotalTime()))
            );
        }

        System.out.println("\n");
    }

    String getDurationString(Long raceTotalTime) {
        final long s = Duration.ofMillis(raceTotalTime).getSeconds();
        return String.format("%d:%02d:%02d", s / 3600, (s % 3600) / 60, (s % 60));
    }
}
