package se233.project2;

import javafx.embed.swing.JFXPanel;
import javafx.scene.input.KeyCode;
import org.junit.jupiter.api.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import se233.project2.view.GameView;
import se233.project2.controller.MediaController;
import se233.project2.controller.SceneController;
import se233.project2.controller.game.DrawingLoop;
import se233.project2.controller.game.GameController;
import se233.project2.controller.game.GameLoop;
import se233.project2.model.Ball;
import se233.project2.model.Character;
import se233.project2.model.GoalRegion;
import se233.project2.controller.GameLoader;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GameTest {
    private Character floatingCharacter;
    private Character standingCharacter;
    private ArrayList<Character> characterListUnderTest;
    private GoalRegion goalRegionOnBallP1;
    private GoalRegion goalRegionP2;
    private ArrayList<GoalRegion> goalRegionUnderTest;
    private GameView gameViewUnderTest;
    private GameLoop gameLoopUnderTest;
    private DrawingLoop drawingLoopUnderTest;
    private Ball ballUnderTest;
    private SceneController sceneControllerUnderTest;
    private Method updateMethod, redrawMethod, collisionMethod;

    @BeforeEach
    public void setup() {
        new JFXPanel();
        GameLoader.load();
        MediaController.getSfxVolume().set(0);
        floatingCharacter = new Character(100d, 50d, KeyCode.A, KeyCode.D,
                KeyCode.W, KeyCode.SPACE, KeyCode.S, "Megaman");
        standingCharacter = new Character(200d, GameView.GROUND - Character.CHARACTER_HEIGHT / 2, KeyCode.LEFT,
                KeyCode.RIGHT,
                KeyCode.UP, KeyCode.ENTER, KeyCode.DOWN, "Megaman");
        characterListUnderTest = new ArrayList<Character>();
        characterListUnderTest.add(floatingCharacter);
        characterListUnderTest.add(standingCharacter);
        goalRegionOnBallP1 = new GoalRegion(395, 0, 405, 600, 2);
        goalRegionP2 = new GoalRegion(200, 95, 210, 105, 1);
        goalRegionUnderTest = new ArrayList<GoalRegion>();
        goalRegionUnderTest.add(goalRegionOnBallP1);
        goalRegionUnderTest.add(goalRegionP2);
        ballUnderTest = new Ball(400d, 100d);
        gameViewUnderTest = new GameView();
        gameViewUnderTest.getCharacters().addAll(characterListUnderTest);
        gameViewUnderTest.getGoalRegions().addAll(goalRegionUnderTest);
        gameViewUnderTest.setBall(ballUnderTest);
        GameController.setTesting(true);
        GameController.setGoalable(true);
        GameController.setGameView(gameViewUnderTest);
        gameLoopUnderTest = new GameLoop(gameViewUnderTest);
        drawingLoopUnderTest = new DrawingLoop(gameViewUnderTest);
        sceneControllerUnderTest = new SceneController();
        Launcher.setSc(sceneControllerUnderTest);
        sceneControllerUnderTest.getKeys().clear();
        try {
            updateMethod = DrawingLoop.class.getDeclaredMethod("update", ArrayList.class);
            redrawMethod = GameLoop.class.getDeclaredMethod("paint", ArrayList.class, Ball.class);
            collisionMethod = GameLoop.class.getDeclaredMethod("checkDrawCollisions", ArrayList.class, Ball.class,
                    ArrayList.class);
            updateMethod.setAccessible(true);
            redrawMethod.setAccessible(true);
            collisionMethod.setAccessible(true);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            updateMethod = null;
            redrawMethod = null;
            collisionMethod = null;
        }
    }

    @Test
    public void characterShouldMoveLeftWhenSetToMoveLeft() throws IllegalAccessException, InvocationTargetException {
        double startX = floatingCharacter.getX();
        floatingCharacter.setDirectionR(false);
        floatingCharacter.setIsMoving(true);
        redrawMethod.invoke(gameLoopUnderTest, characterListUnderTest, ballUnderTest);
        assertTrue(floatingCharacter.getX() < startX, "Character should move left");
    }

    @Test
    public void characterShouldMoveRightWhenSetToMoveRight() throws IllegalAccessException, InvocationTargetException {
        double startX = floatingCharacter.getX();
        floatingCharacter.setDirectionR(true);
        floatingCharacter.setIsMoving(true);
        redrawMethod.invoke(gameLoopUnderTest, characterListUnderTest, ballUnderTest);
        assertTrue(floatingCharacter.getX() > startX, "Character should move right");
    }

    @Test
    public void characterShouldFallWhenInAir() throws IllegalAccessException, InvocationTargetException {
        double startY = floatingCharacter.getY();
        redrawMethod.invoke(gameLoopUnderTest, characterListUnderTest, ballUnderTest);
        redrawMethod.invoke(gameLoopUnderTest, characterListUnderTest, ballUnderTest);
        assertTrue(floatingCharacter.getY() > startY, "Character should fall");
    }

    @Test
    public void characterShouldJumpWhenJump() throws IllegalAccessException, InvocationTargetException {
        double startY = standingCharacter.getY();
        assertTrue(standingCharacter.isMidAir(), "Character should be in mid air");
        collisionMethod.invoke(gameLoopUnderTest, characterListUnderTest, ballUnderTest, goalRegionUnderTest);
        redrawMethod.invoke(gameLoopUnderTest, characterListUnderTest, ballUnderTest);
        assertFalse(standingCharacter.isMidAir(), "Character should be in mid air");
        standingCharacter.jump();
        redrawMethod.invoke(gameLoopUnderTest, characterListUnderTest, ballUnderTest);
        assertTrue(standingCharacter.getY() < startY, "Character should jump");
    }

    @Test
    public void characterShouldKickWhenKickButton() throws IllegalAccessException, InvocationTargetException {
        standingCharacter.kick();
        assertTrue(standingCharacter.isKicking(), "Character should kick");
    }

    @Test
    public void specialShouldBeQueuedWhenGaugeFull() throws IllegalAccessException, InvocationTargetException {
        GameController.getP2Special().set(100);
        GameController.executeSpecial(standingCharacter);
        assertTrue(standingCharacter.isSpecialQueued(), "Special should be queued");
    }

    @Test
    public void specialShouldNotBeQueuedWhenGaugeNotFull() throws IllegalAccessException, InvocationTargetException {
        GameController.getP2Special().set(0);
        GameController.executeSpecial(standingCharacter);
        assertFalse(standingCharacter.isSpecialQueued(), "Special should not be queued");
    }

    @Test
    public void characterCompianceWithKeyPress() throws IllegalAccessException, InvocationTargetException {
        Launcher.getSceneController().getKeys().add(KeyCode.SPACE);
        drawingLoopUnderTest.update(characterListUnderTest);
        assertSame(Launcher.getSceneController().getKeys(), drawingLoopUnderTest.getKeys(), "Same Keys");
        assertTrue(Launcher.getSceneController().getKeys().isPressed(floatingCharacter.getKickKey()),
                "Kick Key Pressed");
        assertTrue(floatingCharacter.isKicking(), "Character should kick");
    }

    @Test
    public void physicsShouldBeAppliedOnBall() throws IllegalAccessException, InvocationTargetException {
        double startY = ballUnderTest.getY();
        collisionMethod.invoke(gameLoopUnderTest, characterListUnderTest, ballUnderTest, goalRegionUnderTest);
        redrawMethod.invoke(gameLoopUnderTest, characterListUnderTest, ballUnderTest);
        redrawMethod.invoke(gameLoopUnderTest, characterListUnderTest, ballUnderTest);
        assertTrue(ballUnderTest.getY() > startY, "Ball should fall");
    }

    @Test
    public void goalShouldBeScoredWhenBallInGoal() throws IllegalAccessException, InvocationTargetException {
        GameController.setGoalable(true);
        collisionMethod.invoke(gameLoopUnderTest, characterListUnderTest, ballUnderTest, goalRegionUnderTest);
        assertTrue(ballUnderTest.intersects_goal_region(goalRegionOnBallP1), "Intersection should be true");
        assertNotEquals(0, GameController.getP2Score().get(), "Goal should be scored");
    }
}
