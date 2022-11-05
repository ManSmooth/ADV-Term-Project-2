package se233.project2.model;

import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import se233.project2.controller.Game.GameController;

public class GoalRegion extends Rectangle2D {
    private int player;

    public GoalRegion(double x1, double y1, double x2, double y2, int player) {
        super(x1, y1, x2 - x1, y2 - y1);
        this.player = player;
    }

    public void goal() {
        if (!GameController.isGoalable())
            return;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if (player == 1)
                    GameController.incrementP1();
                else if (player == 2)
                    GameController.incrementP2();
            }
        });
        GameController.setGoalable(false);
    }
}
