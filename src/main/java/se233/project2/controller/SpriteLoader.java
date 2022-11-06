package se233.project2.controller;

import java.util.HashMap;
import java.util.Set;

import se233.project2.Launcher;
import se233.project2.model.SpriteData;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class SpriteLoader {
    private static HashMap<String, SpriteData> sprites = new HashMap<>();
    private static Logger logger = LogManager.getLogger(SpriteLoader.class);

    public static void load() throws IOException {
        String text = new String(Launcher.class.getResourceAsStream("/assets/data/sprite_data.json").readAllBytes())
                .replaceAll("[\\n\\t]", "");
        JSONArray jsonarray = new JSONArray(text);
        for (int i = 0; i < jsonarray.length(); i++) {
            JSONObject temp_obj = jsonarray.getJSONObject(i);
            sprites.put(temp_obj.getString("name"),
                    new SpriteData(temp_obj.getString("sheetURL"), temp_obj.getJSONArray("animation")));
        }
        logger.info("Sprites loaded");
    }

    public static SpriteData getSpriteData(String key) {
        return sprites.get(key);
    }

    public static Set<String> getSpriteNames() {
        return sprites.keySet();
    }
}
