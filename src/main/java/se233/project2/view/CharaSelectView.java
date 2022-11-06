package se233.project2.view;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import se233.project2.Launcher;
import se233.project2.controller.ImageHandler;
import se233.project2.controller.MediaController;
import se233.project2.controller.SpriteLoader;
import se233.project2.controller.game.GameController;

public class CharaSelectView extends BorderPane {
    private IntegerProperty player;

    public CharaSelectView() {
        player = new SimpleIntegerProperty(1);
        this.getStyleClass().add("chara-select");
        Label title = new Label("Character Select");
        title.getStyleClass().add("title-label");
        Label playerSelect = new Label();
        playerSelect.textProperty().bind(new SimpleStringProperty("Player ").concat(player.asString()));
        playerSelect.getStyleClass().add("player-label");
        VBox titleBox = new VBox(8, title, playerSelect);
        titleBox.setAlignment(Pos.TOP_CENTER);
        this.setTop(titleBox);
        VBox funnyBox = new VBox(8);
        SpriteLoader.getSpriteNames().forEach((name) -> {
            Button selectButton = new Button(name);
            selectButton.setPrefWidth(240);
            ImageView icon = new ImageView(ImageHandler.getImage(String.format("/assets/icon/%sIcon.png", name)));
            icon.setFitHeight(32);
            icon.setPreserveRatio(true);
            selectButton.setGraphic(icon);
            selectButton.setOnAction((x) -> {
                if (player.get() == 1) {
                    GameController.setP1Char(name);
                    player.set(player.get() + 1);
                } else if (player.get() == 2) {
                    GameController.setP2Char(name);
                    Launcher.getSceneController().activate("Game");
                }
                selectButton.setDisable(true);
            });
            selectButton.setAlignment(Pos.CENTER_LEFT);
            funnyBox.getChildren().add(selectButton);
        });
        funnyBox.setAlignment(Pos.CENTER);
        this.setCenter(funnyBox);
    }

    public static void onStart() {
        MediaController.play("penguinlogistics");
    }
}
