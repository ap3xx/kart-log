package com.ap3x.kartlog;

import com.ap3x.kartlog.config.LogConfig;
import com.ap3x.kartlog.model.LapLog;
import com.ap3x.kartlog.model.RacerResult;
import com.ap3x.kartlog.service.LapParser;
import com.ap3x.kartlog.service.PrintService;
import com.ap3x.kartlog.service.ResultService;
import com.ap3x.kartlog.service.impl.LapParserImpl;
import com.ap3x.kartlog.service.impl.PrintServiceImpl;
import com.ap3x.kartlog.service.impl.ResultServiceImpl;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

public class Main {

    private static Logger LOGGER = LogConfig.configLog();

    public static void main(String[] args) {
        LOGGER.info("Started processing Kart Race logs");

        if (args.length != 1) {
            LOGGER.warning("Missing file path...");
            return;
        }

        LapParser parser = new LapParserImpl();
        ResultService service = new ResultServiceImpl();
        PrintService printer = new PrintServiceImpl();

        List<LapLog> logs = parser.processLogFile(args[0]);
        logs.sort(Comparator.comparing(LapLog::getLogTime));
        List<RacerResult> grid = service.getRaceResult(logs);
        printer.printGrid(grid);



        LOGGER.info("Finished processing Kart Race logs");
    }
}
