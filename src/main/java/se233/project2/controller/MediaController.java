package se233.project2.controller;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import se233.project2.Launcher;

public class MediaController {
    private static HashMap<String, Media> mediaMap = new HashMap<>();
    private static MediaPlayer player;
    private static String playing = "";
    private static DoubleProperty bgmVolume = new SimpleDoubleProperty(0.5);
    private static DoubleProperty sfxVolume = new SimpleDoubleProperty(0.5);
    private static Logger logger = LogManager.getLogger(MediaController.class);

    public static void load() {
        mediaMap.put("darkness",
                new Media(Launcher.class.getResource("/assets/audio/Darkness01.mp3").toExternalForm()));
        mediaMap.put("ghosthunter",
                new Media(Launcher.class.getResource("/assets/audio/Ghosthunter01.mp3").toExternalForm()));
        mediaMap.put("penguinlogistics",
                new Media(Launcher.class.getResource("/assets/audio/Penguinlogistics.mp3").toExternalForm()));
        mediaMap.put("applause",
                new Media(Launcher.class.getResource("/assets/sfx/applause.mp3").toExternalForm()));
        mediaMap.put("gameStart",
                new Media(Launcher.class.getResource("/assets/sfx/gameStart.mp3").toExternalForm()));
        mediaMap.put("jump",
                new Media(Launcher.class.getResource("/assets/sfx/jump.mp3").toExternalForm()));
        mediaMap.put("kick",
                new Media(Launcher.class.getResource("/assets/sfx/kick.mp3").toExternalForm()));
        mediaMap.put("bounce",
                new Media(Launcher.class.getResource("/assets/sfx/bounce.mp3").toExternalForm()));
        mediaMap.put("goal",
                new Media(Launcher.class.getResource("/assets/sfx/goal.mp3").toExternalForm()));
        mediaMap.put("death",
                new Media(Launcher.class.getResource("/assets/sfx/death.mp3").toExternalForm()));
        bgmVolume.addListener((observable, oldValue, newValue) -> {
            player.setVolume(newValue.doubleValue());
        });
    }

    public static void play(String name) {
        if (playing.equals(name))
            return;
        if (player != null)
            player.stop();
        player = new MediaPlayer(mediaMap.get(name));
        player.setVolume(bgmVolume.get());
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                player.play();
            }
        }, 500);
        logger.info("Playing " + name);
        playing = name;
        nowPlaying();
    }

    public static void stop() {
        if (player != null)
            player.stop();
        playing = "";
    }

    public static void playSFX(String name) {
        MediaPlayer sfx = new MediaPlayer(mediaMap.get(name));
        sfx.setVolume(sfxVolume.get());
        logger.debug("Playing " + name);
        sfx.play();
    }

    public static void playSFX(String name, long millis) {
        MediaPlayer sfx = new MediaPlayer(mediaMap.get(name));
        sfx.setVolume(sfxVolume.get());
        logger.debug("Playing " + name);
        Timeline tl = new Timeline();
        tl.getKeyFrames().add(new KeyFrame(Duration.millis(0), e -> sfx.play()));
        tl.getKeyFrames().add(new KeyFrame(Duration.millis(millis), new KeyValue(sfx.volumeProperty(), 1)));
        tl.getKeyFrames().add(new KeyFrame(Duration.millis(millis + 250), new KeyValue(sfx.volumeProperty(), 0)));
        tl.getKeyFrames().add(new KeyFrame(Duration.millis(millis + 250), e -> sfx.stop()));
        tl.play();
    }

    public static void nowPlaying() {
        BorderPane pane = new BorderPane();
        HBox track_container = new HBox(8);
        VBox track_display = new VBox();
        track_display.setAlignment(Pos.CENTER_RIGHT);
        track_display.getChildren().addAll(new Label("Now Playing"), new Label(playing));
        ImageView icon = new ImageView(new Image(ImageHandler.getImageAsStream("/assets/icon/music_note.png")));
        icon.setFitHeight(32);
        icon.setPreserveRatio(true);
        track_container.setPadding(new Insets(10, 10, 10, 10));
        track_container.setAlignment(Pos.CENTER_RIGHT);
        track_container.getStyleClass().add("now-playing");
        track_container.getChildren().addAll(track_display, icon);
        pane.setBottom(track_container);
        pane.setOpacity(0);
        ((BorderPane) Launcher.getSceneController().getActiveScene()).setRight(pane);
        track_container.setTranslateX(20);
        FadeTransition ft = new FadeTransition(Duration.millis(1000), pane);
        TranslateTransition tt = new TranslateTransition(Duration.millis(2500), pane);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        tt.setByX(-20f);
        ft.play();
        tt.play();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(new Runnable() {
                    public void run() {
                        FadeTransition ft = new FadeTransition(Duration.millis(1000), pane);
                        TranslateTransition tt = new TranslateTransition(Duration.millis(2500), pane);
                        ft.setFromValue(1.0);
                        ft.setToValue(0.0);
                        tt.setByX(20f);
                        ft.play();
                        tt.play();
                    };
                });
            }
        }, 5000);
        logger.info(String.format("add %s to %s", pane.getClass(),
                Launcher.getSceneController().getActiveScene().getClass()));
    }

    public static DoubleProperty getBgmVolume() {
        return bgmVolume;
    }

    public static DoubleProperty getSfxVolume() {
        return sfxVolume;
    }
}
