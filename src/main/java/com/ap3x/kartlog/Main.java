package com.ap3x.kartlog;

import com.ap3x.kartlog.config.LogConfig;
import com.ap3x.kartlog.model.LogRow;
import com.ap3x.kartlog.processor.LogProcessor;

import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

public class Main {

    private static Logger LOGGER = LogConfig.configLog();

    public static void main(String[] args) {
        LOGGER.info("Started processing Kart Race logs");

        if (args.length != 1) {
            LOGGER.warning("Missing file path...");
            return;
        }

        LogProcessor processor = new LogProcessor();
        List<LogRow> rows = processor.processLogFile(args[0]);
        rows.sort(Comparator.comparing(LogRow::getLogTime));
        rows.forEach(System.out::println);

        LOGGER.info("Finished processing Kart Race logs");
    }
}
