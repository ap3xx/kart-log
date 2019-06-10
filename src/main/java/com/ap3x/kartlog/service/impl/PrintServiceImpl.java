package com.ap3x.kartlog.service.impl;

import com.ap3x.kartlog.model.LapLog;
import com.ap3x.kartlog.model.RacerResult;
import com.ap3x.kartlog.service.PrintService;

import java.time.Duration;
import java.util.List;
import java.util.logging.Logger;

public class PrintServiceImpl implements PrintService {

    private static final Logger LOGGER = Logger.getLogger(PrintServiceImpl.class.getName());

    @Override
    public void printGrid(final List<RacerResult> grid) {
        System.out.println("\n|                                           RACE RESULTS                                           |");
        System.out.println("|##################################################################################################|");

        System.out.println(String.format("| POSITION | %24s | COMPLETED LAPS | TOTAL TIME | %16s | AVG SPEED |", "PILOT", "GAP"));

        grid.forEach(p ->
            System.out.println(
                    String.format(
                            "| %8d | %24s | %14d | %11s| %16s | %9f |",
                            grid.indexOf(p) + 1,
                            p.getPilot(),
                            p.getLastCompleteLap(),
                            getDurationString(p.getRaceTotalTime()),
                            getDurationDifferenceString(p.getTimeAfterFirstRacer()),
                            p.getLapSpeedSum() / p.getLastCompleteLap()
                    )
            )
        );
    }

    @Override
    public void printBestLaps(final List<LapLog> bestLaps) {
        System.out.println("\n|             BEST LAPS                    |");
        System.out.println("|##########################################|");

        System.out.println("| PILOT                    | BEST LAP TIME |");

        bestLaps.forEach(l ->
            System.out.println(
                    String.format(
                            "| %24s | %13s |",
                            l.getPilot(),
                            getDurationString(l.getLapTime())
                    )
            )
        );

        System.out.println("|##########################################|");
        System.out.println("| THE BEST LAP FOR THE RACE                |");

        System.out.println(String.format(
                "| %24s | %13s |",
                bestLaps.get(0).getPilot(),
                getDurationString(bestLaps.get(0).getLapTime())
        ));

        System.out.println("\n");
    }

    String getDurationString(final Long raceTotalTime) {
        final Duration duration = Duration.ofMillis(raceTotalTime);
        final long s = duration.getSeconds();
        return String.format("%d:%02d:%02d.%03d", s / 3600, (s % 3600) / 60, (s % 60), duration.getNano() / 1000000);
    }

    String getDurationString(final Duration lapTime) {
        final long s = lapTime.getSeconds();
        return String.format("%d:%02d:%02d.%03d", s / 3600, (s % 3600) / 60, (s % 60), lapTime.getNano() / 1000000);
    }

    String getDurationDifferenceString(Duration lapTime) {
        if (lapTime == null) lapTime = Duration.ZERO;
        final long s = lapTime.getSeconds();
        return String.format("+%02d:%02d.%03d", (s % 3600) / 60, (s % 60), lapTime.getNano() / 1000000);
    }
}
