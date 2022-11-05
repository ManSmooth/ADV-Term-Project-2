package se233.project2.view;

import java.util.ArrayList;

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
import se233.project2.controller.Game.GameLoop;
import se233.project2.controller.Game.DrawingLoop;
import se233.project2.controller.Game.GameController;
import se233.project2.model.Ball;
import se233.project2.model.Character;
import se233.project2.model.GoalRegion;
import se233.project2.view.Splash.SplashType;

public class GameView extends BorderPane {
    private GameLoop gameLoop;
    private DrawingLoop drawingLoop;
    private Label p1ScoreLabel, p2ScoreLabel, timerLabel;
    public final static int GROUND = SceneController.getHeight() - 75;
    private ProgressBar p1Super, p2Super;
    private ArrayList<Character> characters = new ArrayList<>();
    private ArrayList<GoalRegion> goalRegions = new ArrayList<>();
    private Ball ball;
    private ImageView foreground;

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
        p1Super.progressProperty().bind(GameController.getP1Super().divide(100));
        p1Super.setPrefWidth(480);
        p2Super = new ProgressBar();
        p2Super.setPrefWidth(480);
        p2Super.setScaleX(-1);
        p2Super.progressProperty().bind(GameController.getP2Super().divide(100));
        VBox p1Box = new VBox(8, p1Super, p1ScoreLabel);
        p1Box.setAlignment(Pos.CENTER_RIGHT);
        VBox p2Box = new VBox(8, p2Super, p2ScoreLabel);
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
        GameController.startCountdown(20);
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
}
