package com.ap3x.kartlog.config;

import com.ap3x.kartlog.Main;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class LogConfig {

    public static Logger configLog() {
        try {
            InputStream properties = Main.class.getClassLoader().getResourceAsStream("logging.properties");
            LogManager.getLogManager().readConfiguration(properties);
        } catch (IOException e) {
            Logger.getLogger(LogConfig.class.getName()).severe(e.getMessage());
            e.printStackTrace();
        }
        return Logger.getLogger(Main.class.getName());
    }

}
