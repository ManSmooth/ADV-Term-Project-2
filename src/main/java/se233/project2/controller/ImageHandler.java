package se233.project2.controller;

import java.io.InputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import se233.project2.Launcher;

public class ImageHandler {
    private static Logger logger = LogManager.getLogger(ImageHandler.class);

    public static String getImage(String URL) {
        if (Launcher.class.getResource(URL) == null) {
            logger.info(String.format("Image not found, Requested: %s", URL));
            return Launcher.class.getResource("/assets/img/ichihime-128x128.png").toExternalForm();
        }
        return Launcher.class.getResource(URL).toExternalForm();
    }

    public static InputStream getImageAsStream(String URL) {
        if (Launcher.class.getResource(URL).toExternalForm() == null) {
            logger.info(String.format("Image not found, Requested: %s", URL));
            return Launcher.class.getResourceAsStream("/assets/img/ichihime-128x128.png");
        }
        return Launcher.class.getResourceAsStream(URL);
    }
}
