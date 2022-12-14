package se233.project2.controller.game;

import se233.project2.view.GameView;
import se233.project2.Launcher;
import se233.project2.model.Character;
import se233.project2.model.Keys;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.application.Platform;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.SimpleDoubleProperty;

public class DrawingLoop implements Runnable {
    private GameView gameView;
    private DoubleBinding frameRate;
    private SimpleDoubleProperty timeScale;
    private DoubleBinding interval;
    private boolean running;
    private Logger logger = LogManager.getLogger(DrawingLoop.class);
    private boolean testing;
    private Keys keys;

    public DrawingLoop(GameView gameView) {
        this.gameView = gameView;
        timeScale = new SimpleDoubleProperty(1);
        frameRate = new SimpleDoubleProperty(15).multiply(timeScale);
        interval = new SimpleDoubleProperty(1000d).divide(frameRate);
        running = true;
    }

    public void update(ArrayList<Character> characters) {
        keys = Launcher.getSceneController().getKeys();
        for (int i = 0; i < characters.size(); i++) {
            Character character = characters.get(i);
            if (!GameController.isGoalable() && !testing) {
                if (character.isDoingSpecial()) {
                    character.getImageView().setPlaying("special", character.getDirectionR());
                    character.getImageView().tick();
                    if (character.getImageView().isFinishedLoop()) {
                        GameController.finishSpecial(character);
                    }
                }
                if (character.hasWon()) {
                    character.getImageView().setPlaying("winning", character.getDirectionR());
                    character.getImageView().tick();
                }
                if (keys.isPressed(character.getSpecialKey())) {
                    character.trySpecial();
                }
                continue;
            }
            if (keys.isPressed(character.getLeftKey())) {
                character.setIsMoving(true);
                character.setDirectionR(false);
            } else if (keys.isPressed(character.getRightKey())) {
                character.setIsMoving(true);
                character.setDirectionR(true);
            } else if (!(keys.isPressed(character.getRightKey())
                    ^ keys.isPressed(character.getLeftKey()))) {
                character.setIsMoving(false);
            }
            if (keys.isPressed(character.getUpKey())) {
                character.jump();
            }
            if (keys.isPressed(character.getKickKey())) {
                character.kick();
            }
            if (keys.isPressed(character.getSpecialKey())) {
                character.trySpecial();
            }
            if (character.getVxSign() != 0) {
                character.getImageView().setPlaying("running", character.getDirectionR());
            } else if (character.getVxSign() == 0) {
                character.getImageView().setPlaying("idle", character.getDirectionR());
            }
            if (character.isMidAir()) {
                character.getImageView().setPlaying("jumping", character.getDirectionR());
            }
            if (character.isKicking()) {
                character.getImageView().setPlaying("kicking", character.getDirectionR());
            }
            if (character.hasWon()) {
                character.getImageView().setPlaying("winning", character.getDirectionR());
            }
            character.getImageView().tick();
        }
    }

    @Override
    public void run() {
        while (running) {
            float time = System.currentTimeMillis();
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    update(gameView.getCharacters());
                }
            });
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

    public Keys getKeys() {
        return keys;
    }

    public void setTesting(boolean testing) {
        this.testing = testing;
    }
}
