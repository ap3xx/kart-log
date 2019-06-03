package com.ap3x.kartlog.processor;

import com.ap3x.kartlog.model.LogRow;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static com.ap3x.kartlog.KartLogConstants.DELIMITER;

public class LogProcessor {

    private static final Logger LOGGER = Logger.getLogger(LogProcessor.class.getName());

    public List<LogRow> processLogFile(final String logFilePath) {
        LOGGER.info("Reading " + logFilePath);
        try (BufferedReader reader = new BufferedReader(new FileReader(logFilePath))) {
            return reader.lines().skip(1).map(this::parseLogRow)
                    .filter(l -> !l.getPilotName().equals("FMTERR"))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            LOGGER.severe(e.getMessage());
            return null;
        }
    }

    LogRow parseLogRow(final String rawRow) {
        LOGGER.fine("Parsing row: " + rawRow);
        try {
            final String[] splitRow = rawRow.split(DELIMITER);
            return new LogRow(
                    LocalTime.parse(splitRow[0]),
                    Integer.parseInt(splitRow[1]),
                    splitRow[3],
                    Integer.parseInt(splitRow[4]),
                    getDuration(splitRow[5]),
                    Float.parseFloat(splitRow[6].replace(',', '.'))
            );
        } catch (NumberFormatException | DateTimeParseException | IndexOutOfBoundsException e) {
            LOGGER.severe("Formatting exception: " + e.getMessage());
            LOGGER.severe("Record: " + rawRow);
            return new LogRow("FMTERR");
        }
    }

    Duration getDuration(final String time) {
        final String[] splitMillis = time.split("\\.");
        final String[] splitTime = splitMillis[0].split(":");
        final long millis = splitMillis.length > 1 ? Long.parseLong(splitMillis[1]) : 0L;
        final long seconds = splitTime.length > 1 ? Long.parseLong(splitTime[1]) : Long.parseLong(splitTime[0]);
        final long minutes = splitTime.length > 1 ? Long.parseLong(splitTime[0]) : 0L;

        return Duration.ofMillis(millis).plusSeconds(seconds).plusMinutes(minutes);
    }
}
