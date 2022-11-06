package se233.project2.controller.game;

import java.util.ArrayList;
import org.apache.logging.log4j.Logger;

import javafx.application.Platform;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.SimpleDoubleProperty;
import org.apache.logging.log4j.LogManager;
import se233.project2.Launcher;
import se233.project2.model.Ball;
import se233.project2.model.Character;
import se233.project2.model.GoalRegion;
import se233.project2.model.Keys;
import se233.project2.model.PolarVector;
import se233.project2.view.GameView;
import se233.project2.controller.KeyConfigLoader;

public class GameLoop implements Runnable {
    private GameView gameView;
    private DoubleBinding frameRate;
    private SimpleDoubleProperty timeScale;
    private DoubleBinding interval;
    private boolean running;
    private static Logger logger = LogManager.getLogger(GameLoop.class);

    public GameLoop(GameView gameView) {
        this.gameView = gameView;
        timeScale = new SimpleDoubleProperty(1);
        frameRate = new SimpleDoubleProperty(120).multiply(timeScale);
        interval = new SimpleDoubleProperty(1000d).divide(frameRate);
        running = true;
    }

    public void checkDrawCollisions(ArrayList<Character> characters, Ball ball, ArrayList<GoalRegion> goalRegions) {
        for (int i = 0; i < characters.size(); i++) {
            Character c = characters.get(i);
            if(!c.isVisible()) continue;
            c.checkReachGameWall();
            c.checkReachFloor();
            ball.checkIntersectCharacter(c);
        }
        characters.get(0).checkIntersectCharacter(characters.get(1));

        for (int i = 0; i < goalRegions.size(); i++) {
            GoalRegion g = goalRegions.get(i);
            ball.checkIntersectGoalRegion(g);
        }
        ball.checkReachGameWall();
        ball.checkReachFloor();
    }

    public void paint(ArrayList<Character> characters, Ball ball) {
        for (int i = 0; i < characters.size(); i++) {
            Character c = characters.get(i);
            c.move();
            c.updatePos();
        }
        ball.move();
        ball.updatePos();
    }

    public void checkHealth(ArrayList<Character> characters) {
        for (int i = 0; i < characters.size(); i++) {
            Character c = characters.get(i);
            if (c.getHp().get() <= 0 && c.isVisible())
                GameController.processDeath(i);
        }
    }

    public void funnyBallCheck(Ball ball) {
        Keys keys = Launcher.getSceneController().getKeys();
        if (keys.isPressed(KeyConfigLoader.getKeyConfig().get("ball_up"))) {
            ball.setVel(ball.getVel().add(new PolarVector().fromPolar(3, -Math.PI / 2)));
            ball.setMidAir(true);
        }
        if (keys.isPressed(KeyConfigLoader.getKeyConfig().get("ball_down"))) {
            ball.setVel(ball.getVel().add(new PolarVector().fromPolar(1, Math.PI / 2)));
        }
        if (keys.isPressed(KeyConfigLoader.getKeyConfig().get("ball_left"))) {
            ball.setVel(ball.getVel().add(new PolarVector().fromPolar(1, Math.PI)));
        }
        if (keys.isPressed(KeyConfigLoader.getKeyConfig().get("ball_right"))) {
            ball.setVel(ball.getVel().add(new PolarVector().fromPolar(1, 0)));
        }

    }

    @Override
    public void run() {
        while (running) {
            long time = System.currentTimeMillis();
            if (GameController.isGoalable()) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        GameController.incrementSP1(10.0 / frameRate.get());
                        GameController.incrementSP2(10.0 / frameRate.get());
                        checkDrawCollisions(gameView.getCharacters(), gameView.getBall(), gameView.getGoalRegions());
                        paint(gameView.getCharacters(), gameView.getBall());
                        checkHealth(gameView.getCharacters());
                        funnyBallCheck(gameView.getBall());
                    }
                });
            }
            time = System.currentTimeMillis() - time;
            if (interval.get() > 1000)
                continue;
            try {
                if (time < interval.get()) {
                    Thread.sleep((long) (interval.get() - time));
                } else {
                    Thread.sleep((long) (interval.get() - (interval.get() % time)));
                }
            } catch (InterruptedException e) {
                logger.error(e.getCause(), e);
            }
        }
    }

    public DoubleBinding getFrameRate() {
        return frameRate;
    }

    public void setTimeScale(double timeScale) {
        this.timeScale.set(timeScale);
        frameRate.getValue();
        interval.getValue();
        logger.info(String.format("%s %s", frameRate, timeScale));
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
}
