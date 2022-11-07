package se233.project2.view;

import javafx.beans.property.DoubleProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import se233.project2.controller.ImageHandler;
import se233.project2.controller.SceneController;
import se233.project2.controller.game.GameController;

public class Splash extends BorderPane {
    public static enum SplashType {
        GOAL, P1WIN, P2WIN, DRAW, PAUSE
    }

    private ImageView splashImage;

    public Splash(SplashType type) {
        this.setOpacity(0);
        this.getStyleClass().add("splash");
        this.setPrefSize(SceneController.getWidth(), SceneController.getHeight());
        switch (type) {
            case GOAL:
                splashImage = new ImageView(ImageHandler.getImage("/assets/img/goal.png"));
                break;
            case P1WIN:
                splashImage = new ImageView(ImageHandler.getImage("/assets/img/p1win.png"));
                break;
            case P2WIN:
                splashImage = new ImageView(ImageHandler.getImage("/assets/img/p2win.png"));
                break;
            case DRAW:
                splashImage = new ImageView(ImageHandler.getImage("/assets/img/draw.png"));
                break;
            case PAUSE:
                splashImage = new ImageView(ImageHandler.getImage("/assets/img/paused.png"));
                break;
        }
        if (!type.equals(SplashType.PAUSE))
            this.setCenter(splashImage);
        else {
            Button resumeButton = new Button("Resume");
            resumeButton.setOnAction(e -> GameController.callResume());
            resumeButton.setPrefWidth(240);
            Button exitButton = new Button("Exit");
            exitButton.setOnAction(e -> GameController.cleanUp());
            exitButton.setPrefWidth(240);
            VBox buttonBox = new VBox(8, splashImage, resumeButton, exitButton);
            buttonBox.setAlignment(Pos.CENTER);
            this.setCenter(buttonBox);
        }
    }

    public DoubleProperty getFade() {
        return this.opacityProperty();
    }
}
