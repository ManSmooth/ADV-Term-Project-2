package se233.project2.view;

import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import se233.project2.Launcher;
import se233.project2.controller.ImageHandler;
import se233.project2.controller.MediaController;

public class MenuView extends BorderPane {
    public static final CornerRadii CORNER = new CornerRadii(10);

    public MenuView() {
        this.getStyleClass().addAll("container", "menu");
        this.getChildren().clear();
        VBox container = new VBox();
        ImageView title = new ImageView(ImageHandler.getImage("/assets/img/titleCard.png"));
        Button startButton = new Button("Start");
        startButton.setPrefWidth(240);
        startButton.setOnAction((x) -> {
            Launcher.getSceneController().activate("CharaSelect");
        });
        Button optionButton = new Button("Options");
        optionButton.setPrefWidth(240);
        optionButton.setOnAction((x) -> {
            createOptionsPane();
        });
        Button exitButton = new Button("Exit");
        exitButton.setPrefWidth(240);
        exitButton.setOnAction((x) -> {
            Platform.exit();
        });
        container.getChildren().addAll(title, startButton, optionButton, exitButton);
        container.setSpacing(12);
        container.setAlignment(Pos.TOP_LEFT);
        container.setPadding(new Insets(20, 20, 20, 20));
        this.setLeft(container);
    }

    public static void onStart() {
        MediaController.playSFX("gameStart");
        MediaController.play("darkness");
    }

    public void createOptionsPane() {
        OptionsPane options = new OptionsPane(this);
        this.setCenter(options);
        options.requestFocus();
    }

    public void removeOptionsPane(OptionsPane o) {
        this.getChildren().remove(o);
    }
}
