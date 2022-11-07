package se233.project2.controller.game;

import se233.project2.view.CharaSelectView;
import se233.project2.view.GameView;
import se233.project2.view.Splash;
import se233.project2.view.Splash.SplashType;
import se233.project2.model.Ball;
import se233.project2.model.Character;
import se233.project2.model.GoalRegion;
import se233.project2.Launcher;
import se233.project2.controller.KeyConfigLoader;
import se233.project2.controller.MediaController;
import se233.project2.controller.SceneController;

import java.util.ArrayList;
import java.util.HashMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.input.KeyCode;
import javafx.util.Duration;

public class GameController {
    public static final double GRAVITY = 0.75;
    public static final double FRICTION = 0.85;
    public static final double AIR_RESISTANCE = 0.95;
    public static final double ACCELERATION = 0.75;
    public static final double JUMP_VELOCITY = GRAVITY * -30;
    public static final double ELASTICITY = 0.9;
    public static final double PHYSICS_FACTOR = 1;
    private static String p1Char = "Enker";
    private static String p2Char = "Protoman";
    private static SimpleIntegerProperty p1Score = new SimpleIntegerProperty(0);
    private static SimpleIntegerProperty p2Score = new SimpleIntegerProperty(0);
    private static SimpleDoubleProperty p1Special = new SimpleDoubleProperty(0);
    private static SimpleDoubleProperty p2Special = new SimpleDoubleProperty(0);
    private static SimpleIntegerProperty roundTime = new SimpleIntegerProperty(0);
    private static HashMap<String, KeyCode> keyConfig = KeyConfigLoader.getKeyConfig();;
    private static Logger logger = LogManager.getLogger(GameController.class);
    private static BooleanProperty goalable = new SimpleBooleanProperty(true);
    private static GameView gameView;
    private static boolean testing;
    private static ArrayList<Timeline> tempTl = new ArrayList<Timeline>();
    private static Timeline roundTimer;

    public static void startGame(GameView _gameView) {
        goalable.set(true);
        gameView = _gameView;
        p1Score.set(0);
        p2Score.set(0);
        p1Special.set(0);
        p2Special.set(0);
        roundTime.set(0);
        setupGame(gameView);
    }

    public static void setupGame(GameView gameView) {
        gameView.getCharacters().add(new Character(100d, 50d, keyConfig.get("p1_left"), keyConfig.get("p1_right"),
                keyConfig.get("p1_jump"), keyConfig.get("p1_kick"), keyConfig.get("p1_special"), p1Char));
        gameView.getCharacters()
                .add(new Character(SceneController.getWidth() - 100d, 50d, keyConfig.get("p2_left"),
                        keyConfig.get("p2_right"), keyConfig.get("p2_jump"), keyConfig.get("p2_kick"),
                        keyConfig.get("p2_special"), p2Char));
        gameView.getGoalRegions().add(new GoalRegion(0, 500, 100, GameView.GROUND, 2));
        gameView.getGoalRegions().add(new GoalRegion(SceneController.getWidth() - 100, 500,
                SceneController.getWidth(), GameView.GROUND, 1));
        gameView.setBall(new Ball(SceneController.getWidth() / 2, 50));
        gameView.getChildren().addAll(gameView.getBall());
        gameView.getChildren().addAll(gameView.getCharacters());
        if (gameView.getForeground() != null)
            gameView.getForeground().toFront();
    }

    public static void resetGame(GameView gameView) {
        tempTl.forEach(tl -> tl.stop());
        tempTl.clear();
        Platform.runLater(() -> {
            gameView.getCharacters().get(0).reset(100d, 50d);
            gameView.getCharacters().get(0).setVisible(true);
            gameView.getCharacters().get(1).reset(SceneController.getWidth() - 100d, 50d);
            gameView.getCharacters().get(1).setVisible(true);
            gameView.getBall().reset(SceneController.getWidth() / 2, 50);
        });
        goalable.set(true);
    }

    public static void incrementP1() {
        p1Score.set(p1Score.get() + 1);
        logger.info(String.format("P1 Goal - [%d - %d]", p1Score.get(), p2Score.get()));
        if (!testing)
            playGoal();
    }

    public static void incrementP2() {
        p2Score.set(p2Score.get() + 1);
        logger.info(String.format("P2 Goal - [%d - %d]", p1Score.get(), p2Score.get()));
        if (!testing)
            playGoal();
    }

    public static void incrementSP1(double d) {
        p1Special.set(p1Special.get() + d);
    }

    public static void incrementSP2(double d) {
        p2Special.set(p2Special.get() + d);
    }

    public static void playGoal() {
        Splash splash = gameView.createSplash(SplashType.GOAL);
        Timeline timeline = new Timeline();
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(500),
                new KeyValue(splash.getFade(), 1)));
        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1), (e) -> {
            resetGame(gameView);
            splash.getFade().set(0);
            gameView.getChildren().remove(splash);
        }));
        MediaController.playSFX("goal", 1000);
        tempTl.add(timeline);
        timeline.play();
    }

    public static SimpleIntegerProperty getP1Score() {
        return p1Score;
    }

    public static SimpleIntegerProperty getP2Score() {
        return p2Score;
    }

    public static SimpleDoubleProperty getP1Special() {
        return p1Special;
    }

    public static SimpleDoubleProperty getP2Special() {
        return p2Special;
    }

    public static void pauseGame() {
        tempTl.forEach(tl -> tl.pause());
        setGoalable(false);
    }

    public static void resumeGame() {
        tempTl.forEach(tl -> tl.play());
        setGoalable(true);
    }

    public static void callPause() {
        if (gameView.isPaused()) {
            callResume();
            return;
        }
        gameView.setMenuSplash(gameView.createSplash(SplashType.PAUSE));
        gameView.getMenuSplash().getFade().set(1);
        GameController.pauseGame();
        gameView.setPaused(true);
    }

    public static void callResume() {
        gameView.getChildren().remove(gameView.getMenuSplash());
        gameView.getMenuSplash().getFade().set(0);
        GameController.resumeGame();
        gameView.setPaused(false);
    }

    public static void startCountdown(int time) {
        roundTime.set(time);
        roundTimer = new Timeline(new KeyFrame(Duration.seconds(time), new KeyValue(roundTime, 0)));
        roundTimer.getKeyFrames().add(new KeyFrame(Duration.seconds(time), (e) -> {
            goalable.set(false);
            endRound();
        }));
        roundTimer.play();
        goalable.addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                roundTimer.pause();
            } else {
                roundTimer.play();
            }
        });
    }

    public static SimpleIntegerProperty getRoundTime() {
        return roundTime;
    }

    private static void endRound() {
        gameView.getDrawingLoop().setTimeScale(0.1);
        MediaController.stop();
        MediaController.playSFX("applause", 5000);
        if (p1Score.get() > p2Score.get()) {
            Splash splash = gameView.createSplash(SplashType.P1WIN);
            gameView.getCharacters().get(0).setWon(true);
            Timeline timeline2 = new Timeline();
            timeline2.getKeyFrames().add(new KeyFrame(Duration.millis(500),
                    new KeyValue(splash.getFade(), 1)));
            timeline2.getKeyFrames().add(new KeyFrame(Duration.seconds(5), (e2) -> {
                cleanUp();
            }));
            timeline2.play();
        } else if (p1Score.get() < p2Score.get()) {
            Splash splash = gameView.createSplash(SplashType.P2WIN);
            gameView.getCharacters().get(1).setWon(true);
            Timeline timeline2 = new Timeline();
            timeline2.getKeyFrames().add(new KeyFrame(Duration.millis(500),
                    new KeyValue(splash.getFade(), 1)));
            timeline2.getKeyFrames().add(new KeyFrame(Duration.seconds(5), (e2) -> {
                cleanUp();
            }));
            timeline2.play();
        } else {
            Splash splash = gameView.createSplash(SplashType.DRAW);
            Timeline timeline2 = new Timeline();
            timeline2.getKeyFrames().add(new KeyFrame(Duration.millis(500),
                    new KeyValue(splash.getFade(), 1)));
            timeline2.getKeyFrames().add(new KeyFrame(Duration.seconds(5), (e2) -> {
                cleanUp();
            }));
            timeline2.play();
        }
    }

    public static void cleanUp() {
        gameView.getDrawingLoop().setRunning(false);
        gameView.getGameLoop().setRunning(false);
        Launcher.getSceneController().activate("Menu");
        Launcher.getSceneController().removeScene("Game");
        Launcher.getSceneController().addScene("Game", new GameView());
        Launcher.getSceneController().removeScene("CharaSelect");
        Launcher.getSceneController().addScene("CharaSelect", new CharaSelectView());
    }

    public static boolean isGoalable() {
        return goalable.get();
    }

    public static BooleanProperty goalableProperty() {
        return goalable;
    }

    public static void setGoalable(boolean goalable) {
        GameController.goalable.set(goalable);
    }

    public static void executeSpecial(Character c) {
        int player = gameView.getCharacters().indexOf(c);
        logger.info(String.format("Player %d: Try Special", player + 1));
        if (player == 0) {
            if (p1Special.get() >= 100) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        gameView.getP1Super().setStyle("-fx-accent: skyblue;");
                    }
                });
                logger.info(String.format("Player %d: Queuing Special", player + 1));
                c.queueSpecial();
            }
        } else if (player == 1) {
            if (p2Special.get() >= 100) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        gameView.getP2Super().setStyle("-fx-accent: skyblue;");
                    }
                });
                logger.info(String.format("Player %d: Queuing Special", player + 1));
                c.queueSpecial();
            }
        }
    }

    public static void exhaustSpecial(Character c) {
        int player = gameView.getCharacters().indexOf(c);
        logger.info(String.format("Player %d: Exhausting Special", player + 1));
        if (player == 0) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    Timeline tl = new Timeline(new KeyFrame(Duration.millis(250), new KeyValue(p1Special, 0)));
                    tl.getKeyFrames().add(new KeyFrame(Duration.millis(250),
                            e -> gameView.getP1Super().setStyle("-fx-accent: blue;")));
                    tl.play();
                }
            });
        } else if (player == 1) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    Timeline tl = new Timeline(new KeyFrame(Duration.millis(250), new KeyValue(p2Special, 0)));
                    tl.getKeyFrames().add(new KeyFrame(Duration.millis(250),
                            e -> gameView.getP2Super().setStyle("-fx-accent: blue;")));
                    tl.play();
                }
            });
        }
    }

    public static void processDeath(int i) {
        Timeline timeline = new Timeline();
        timeline.getKeyFrames()
                .add(new KeyFrame(Duration.millis(0), e -> gameView.getCharacters().get(i).setVisible(false)));
        timeline.getKeyFrames()
                .add(new KeyFrame(Duration.millis(3000), e -> {
                    gameView.getCharacters().get(i).setVisible(true);
                    gameView.getCharacters().get(i).reset(i == 0 ? 100d : SceneController.getWidth() - 100d, 50d);
                }));
        MediaController.playSFX("death");
        tempTl.add(timeline);
        timeline.play();
    }

    public static void doSpecialAnimation(Character c) {
        gameView.getDrawingLoop().setTimeScale(0.25);
        gameView.getGameLoop().setTimeScale(0.25);
        goalable.set(false);
        c.setDoingSpecial(true);
    }

    public static void finishSpecial(Character c) {
        gameView.getDrawingLoop().setTimeScale(1);
        gameView.getGameLoop().setTimeScale(1);
        goalable.set(true);
        Launcher.getSceneController().getKeys().clear();
        c.setIsMoving(false);
        c.setDoingSpecial(false);
    }

    public static void setP1Char(String p1Char) {
        GameController.p1Char = p1Char;
    }

    public static void setP2Char(String p2Char) {
        GameController.p2Char = p2Char;
    }

    public static void setGameView(GameView gameView) {
        GameController.gameView = gameView;
    }

    public static void setTesting(boolean testing) {
        GameController.testing = testing;
    }
}
