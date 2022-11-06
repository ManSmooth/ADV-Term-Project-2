package se233.project2.controller;

import java.io.IOException;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import javafx.scene.input.KeyCode;
import se233.project2.Launcher;

public class KeyConfigLoader {
    private static Logger logger = LogManager.getLogger(KeyConfigLoader.class);
    private static HashMap<String, KeyCode> keyConfig = new HashMap<>();

    public static void load() throws IOException {
        String text = new String(Launcher.class.getResourceAsStream("/keyconfig.json").readAllBytes())
                .replaceAll("[\\n\\t]", "");
        JSONObject json = new JSONObject(text);
        for (String keySet : json.keySet()) {
            keyConfig.put(keySet, KeyCode.valueOf(json.getString(keySet)));
        }
        logger.info("Successfully Registered Keys");
    }

    public static HashMap<String, KeyCode> getKeyConfig() {
        return keyConfig;
    }
}
