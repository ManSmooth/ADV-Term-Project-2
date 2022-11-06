package se233.project2.controller;

import java.io.IOException;
import org.apache.logging.log4j.Logger;

import javafx.application.Platform;

import org.apache.logging.log4j.LogManager;

public class GameLoader {
    private static Logger logger = LogManager.getLogger(GameLoader.class);
    static {
        load();
    }

    public static void load() {
        try {
            SpriteLoader.load();
            MediaController.load();
            KeyConfigLoader.load();
        } catch (IOException | NullPointerException e) {
            logger.fatal(e.getCause(), e);
            Platform.exit();
        }
    }
}
