package se233.project2.view;

import javafx.beans.property.DoubleProperty;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import se233.project2.controller.ImageHandler;
import se233.project2.controller.SceneController;

public class Splash extends BorderPane {
    public static enum SplashType {
        GOAL, P1WIN, P2WIN, DRAW
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
        }
        this.setCenter(splashImage);
    }

    public DoubleProperty getFade() {
        return this.opacityProperty();
    }
}
