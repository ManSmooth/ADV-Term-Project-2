package se233.project2.view;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import se233.project2.controller.MediaController;

public class OptionsPane extends BorderPane {

    public OptionsPane(MenuView menu) {
        this.getStyleClass().add("options-pane");
        this.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ESCAPE) {
                menu.removeOptionsPane(this);
            }
        });
        Label bgmLabel = new Label("BGM Volume");
        bgmLabel.setAlignment(Pos.CENTER_LEFT);
        Slider bgmSlider = new Slider(0, 1, 0);
        bgmSlider.valueProperty().bindBidirectional(MediaController.getBgmVolume());
        Label sfxLabel = new Label("SFX Volume");
        sfxLabel.setAlignment(Pos.CENTER_LEFT);
        Slider sfxSlider = new Slider(0, 1, 0);
        sfxSlider.valueProperty().bindBidirectional(MediaController.getSfxVolume());
        VBox container = new VBox(8, bgmLabel, bgmSlider, sfxLabel, sfxSlider);
        container.setAlignment(Pos.CENTER);
        this.setCenter(container);
    }
}
