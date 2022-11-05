package se233.project2.view;

import javafx.scene.layout.BorderPane;
import se233.project2.controller.MediaController;

public class CharaSelectView extends BorderPane {
    public CharaSelectView() {
        this.getStyleClass().add("chara-select-view");
    }

    public static void onStart() {
        MediaController.play("penguinlogistics");
    }
}
