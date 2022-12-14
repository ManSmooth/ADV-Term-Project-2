package se233.project2;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import se233.project2.controller.GameLoader;
import se233.project2.controller.SceneController;

public class Launcher extends Application {
    private static Stage primaryStage;
    private static SceneController sc;
    private static Logger logger = LogManager.getLogger(Launcher.class.getName());

    public static void launch_main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage _primaryStage) {
        GameLoader.load();
        primaryStage = _primaryStage;
        primaryStage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });
        primaryStage.setOnHidden(e -> {
            Platform.exit();
            System.exit(0);
        });
        sc = new SceneController();
        sc.activate("Menu");
        try {
            sc.getScene().getStylesheets().add(this.getClass().getResource("/style.css").toExternalForm());
        } catch (NullPointerException e) {
            logger.warn("Style sheet not found");
        }
        primaryStage.setResizable(false);
        primaryStage.setTitle("SE233 Project 2: Head Soccer");
        primaryStage.setScene(sc.getScene());
        primaryStage.show();
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static SceneController getSceneController() {
        if (sc == null)
            sc = new SceneController();
        return sc;
    }

    public static void setSc(SceneController sc) {
        Launcher.sc = sc;
    }
}
