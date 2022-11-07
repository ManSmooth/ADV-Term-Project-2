package se233.project2.view;

import java.util.ArrayList;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import se233.project2.controller.ImageHandler;
import se233.project2.controller.MediaController;
import se233.project2.controller.SceneController;
import se233.project2.controller.game.DrawingLoop;
import se233.project2.controller.game.GameController;
import se233.project2.controller.game.GameLoop;
import se233.project2.model.Ball;
import se233.project2.model.Character;
import se233.project2.model.GoalRegion;
import se233.project2.view.Splash.SplashType;

public class GameView extends BorderPane {
    private GameLoop gameLoop;
    private DrawingLoop drawingLoop;
    private Label p1ScoreLabel, p2ScoreLabel, timerLabel;
    public final static int GROUND = SceneController.getHeight() - 75;
    private ProgressBar p1Super, p2Super, p1Hp, p2Hp;
    private ArrayList<Character> characters = new ArrayList<>();
    private ArrayList<GoalRegion> goalRegions = new ArrayList<>();
    private Ball ball;
    private ImageView foreground;
    private boolean paused = false;
    private Splash menuSplash;

    public GameView() {
        this.getStyleClass().add("game-view");
    }

    private void start() {
        HBox containerBox = new HBox(64);
        GameController.startGame(this);
        p1ScoreLabel = new Label("ttt");
        p1ScoreLabel.getStyleClass().add("score-label");
        p1ScoreLabel.textProperty().bind(GameController.getP1Score().asString());
        p2ScoreLabel = new Label("ttt");
        p2ScoreLabel.getStyleClass().add("score-label");
        p2ScoreLabel.textProperty().bind(GameController.getP2Score().asString());
        timerLabel = new Label("ttt");
        timerLabel.getStyleClass().add("time-label");
        timerLabel.setPrefWidth(120);
        timerLabel.setAlignment(Pos.CENTER);
        timerLabel.textProperty().bind(GameController.getRoundTime().asString());
        p1Super = new ProgressBar();
        p1Super.progressProperty().bind(GameController.getP1Special().divide(100));
        p1Super.setPrefWidth(480);
        p2Super = new ProgressBar();
        p2Super.setPrefWidth(480);
        p2Super.setScaleX(-1);
        p2Super.progressProperty().bind(GameController.getP2Special().divide(100));
        p1Hp = new ProgressBar();
        p1Hp.getStyleClass().add("health-bar");
        p1Hp.progressProperty().bind(getCharacters().get(0).getHp().divide(100));
        getCharacters().get(0).getHp().addListener((obs, oldVal, newVal) -> {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    if (newVal.doubleValue() <= 25) {
                        p1Hp.setStyle("-fx-accent: red");
                    } else if (newVal.doubleValue() <= 50) {
                        p1Hp.setStyle("-fx-accent: gold");
                    } else {
                        p1Hp.setStyle("-fx-accent: green");
                    }
                }
            });
        });
        p1Hp.setPrefWidth(320);
        p2Hp = new ProgressBar();
        p2Hp.getStyleClass().add("health-bar");
        p2Hp.setPrefWidth(320);
        p2Hp.setScaleX(-1);
        p2Hp.progressProperty().bind(getCharacters().get(1).getHp().divide(100));
        getCharacters().get(1).getHp().addListener((obs, oldVal, newVal) -> {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    if (newVal.doubleValue() <= 25) {
                        p2Hp.setStyle("-fx-accent: red");
                    } else if (newVal.doubleValue() <= 50) {
                        p2Hp.setStyle("-fx-accent: gold");
                    } else {
                        p2Hp.setStyle("-fx-accent: green");
                    }
                }
            });
        });

        BorderPane p1Lower = new BorderPane();
        p1Lower.setLeft(p1Hp);
        p1Lower.setRight(p1ScoreLabel);
        BorderPane p2Lower = new BorderPane();
        p2Lower.setRight(p2Hp);
        p2Lower.setLeft(p2ScoreLabel);
        VBox p1Box = new VBox(8, p1Super, p1Lower);
        p1Box.setAlignment(Pos.CENTER_RIGHT);
        VBox p2Box = new VBox(8, p2Super,
                p2Lower);
        p2Box.setAlignment(Pos.CENTER_LEFT);
        containerBox.setAlignment(Pos.CENTER);
        containerBox.getChildren().addAll(p1Box, timerLabel, p2Box);
        foreground = new ImageView(ImageHandler.getImage("/assets/background/BGfront.gif"));
        foreground.setFitWidth(SceneController.getWidth());
        foreground.setPreserveRatio(true);
        this.setTop(containerBox);
        this.getChildren().add(foreground);
    }

    public ArrayList<Character> getCharacters() {
        return characters;
    }

    public Splash createSplash(SplashType type) {
        Splash splash = new Splash(type);
        this.setCenter(splash);
        splash.toFront();
        return splash;
    }

    public void onStart() {
        start();
        gameLoop = new GameLoop(this);
        (new Thread(gameLoop)).start();
        drawingLoop = new DrawingLoop(this);
        (new Thread(drawingLoop)).start();
        MediaController.play("ghosthunter");
        GameController.startCountdown(60);
    }

    public Ball getBall() {
        return ball;
    }

    public void setBall(Ball ball) {
        this.ball = ball;
    }

    public GameLoop getGameLoop() {
        return gameLoop;
    }

    public DrawingLoop getDrawingLoop() {
        return drawingLoop;
    }

    public ArrayList<GoalRegion> getGoalRegions() {
        return goalRegions;
    }

    public ImageView getForeground() {
        return foreground;
    }

    public ProgressBar getP1Super() {
        return p1Super;
    }

    public ProgressBar getP2Super() {
        return p2Super;
    }

    public Splash getMenuSplash() {
        return menuSplash;
    }

    public void setMenuSplash(Splash menuSplash) {
        this.menuSplash = menuSplash;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public boolean isPaused() {
        return paused;
    }
}
