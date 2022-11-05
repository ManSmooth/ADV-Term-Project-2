package se233.project2.view;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import se233.project2.Launcher;
import se233.project2.controller.MediaController;

public class MenuView extends BorderPane {
    public static final CornerRadii CORNER = new CornerRadii(10);

    public MenuView() {
        this.getStyleClass().addAll("container", "menu");
        this.getChildren().clear();
        VBox container = new VBox();
        Label title = new Label("Head Soccer");
        title.getStyleClass().add("title-label");
        title.setAlignment(Pos.TOP_CENTER);
        Button startButton = new Button("Start");
        startButton.setPrefWidth(240);
        startButton.setOnAction((x) -> {
            Launcher.getSceneController().activate("Game");
        });
        Button exitButton = new Button("Exit");
        exitButton.setPrefWidth(240);
        exitButton.setOnAction((x) -> {
            Platform.exit();
        });
        container.getChildren().addAll(title, startButton, exitButton);
        container.setSpacing(12);
        container.setAlignment(Pos.CENTER_LEFT);
        container.setPadding(new Insets(20, 20, 20, 20));
        this.setLeft(container);
    }

    public static void onStart() {
        MediaController.playSFX("gameStart");
        MediaController.play("darkness");
    }
}
