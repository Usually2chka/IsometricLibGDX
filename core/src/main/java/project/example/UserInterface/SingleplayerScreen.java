package project.example.UserInterface;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.List;

import project.example.Game.Entitys.Unit;
import project.example.Network.GameClient;
import project.example.Utils.TextureManager;

public class SingleplayerScreen implements Screen {
    private final Game game;
    private final SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private Viewport viewport;

    // Scene2D elements
    private Stage stage;
    private Skin skin = TextureManager.GetInstance().GetSkin();;
    private TextArea gameLogArea;
    private Dialog gameOverDialog;

    private Unit player;
    private Unit enemy;
    private boolean isPlayerTurn = true;
    private boolean gameOver = false;
    private Unit selectedUnit = null;
    private List<GridPosition> validMoves = new ArrayList<>();

    // Grid Parameters
    private static final int TILE_SIZE = 120;
    private static final int GRID_WIDTH = 5;
    private static final int GRID_HEIGHT = 5;
    private static final int GRID_OFFSET_X = 200;

    private static final int PLAYER_START_X = 1;
    private static final int PLAYER_START_Y = 1;
    private static final int ENEMY_START_X = 3;
    private static final int ENEMY_START_Y = 3;

    private TextButton btnMoveLeft, btnMoveRight, btnMoveUp, btnMoveDown, btnAttack;
    private static class GridPosition {
        int x, y;
        public GridPosition(int x, int y) {
            this.x = x;
            this.y = y;
        }
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            GridPosition that = (GridPosition) o;
            return x == that.x && y == that.y;
        }
        @Override
        public int hashCode() {
            return 31 * x + y;
        }
    }

    public SingleplayerScreen(Game game, SpriteBatch batch) {
        this.game = game;
        this.batch = batch;

        shapeRenderer = new ShapeRenderer();
        viewport = new ScreenViewport();

        player = new Unit(PLAYER_START_X, PLAYER_START_Y, Color.BLUE, "Player");
        enemy = new Unit(ENEMY_START_X, ENEMY_START_Y, Color.RED, "Enemy");

        stage = new Stage(new ScreenViewport(), batch);
        Gdx.input.setInputProcessor(stage);

        setupSkinAndButtons();
        setupLayout();
        setupWorldInputListener();

        logMessage("Game started. Turn: " + player.name);
    }

    private void setupSkinAndButtons() {
        // 5. Button initialization
        btnMoveLeft = new TextButton("LEFT", skin);
        btnMoveRight = new TextButton("RIGHT", skin);
        btnMoveUp = new TextButton("UP", skin);
        btnMoveDown = new TextButton("DOWN", skin);
        btnAttack = new TextButton("ATTACK", skin);

        btnMoveLeft.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                movePlayer(-1, 0);
                clearSelection();
            }
        });
        btnMoveRight.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                movePlayer(1, 0);
                clearSelection();
            }
        });
        btnMoveUp.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                movePlayer(0, 1);
                clearSelection();
            }
        });
        btnMoveDown.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                movePlayer(0, -1);
                clearSelection();
            }
        });
        btnAttack.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                attackEnemy();
                clearSelection();
            }
        });
    }

    private void setupLayout() {
        Table rootTable = new Table();
        rootTable.setFillParent(true);

        gameLogArea = new TextArea("Game Log:\n", skin);
        gameLogArea.setDisabled(true);

        rootTable.add().expandX();
        rootTable.add(gameLogArea).width(600).height(600).pad(10).top().right().row();

        rootTable.add().expand().colspan(2).row();

        Table movementTable = new Table();
        movementTable.add(btnMoveUp).size(80, 80).colspan(3).row();
        movementTable.add(btnMoveLeft).size(80, 80);
        movementTable.add().size(80, 80);
        movementTable.add(btnMoveRight).size(80, 80).row();
        movementTable.add(btnMoveDown).size(80, 80).colspan(3);

        Table actionTable = new Table();
        actionTable.add(btnAttack).size(160, 160).pad(20);

        Table bottomTable = new Table();
        bottomTable.add(movementTable).pad(20).left();
        bottomTable.add().expandX();
        bottomTable.add(actionTable).pad(20).right();

        rootTable.add(bottomTable).fillX().colspan(2).row();

        stage.addActor(rootTable);
    }

    private void logMessage(String message) {
        Gdx.app.log("GameLog", message);
        gameLogArea.setText(gameLogArea.getText() + "\n" + message);
        //gameLogArea.setCursorAt(gameLogArea.getText().length());
    }

    private void setupWorldInputListener() {
        stage.getRoot().addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                // Convert screen coordinates (x, y) to world coordinates
                Vector3 worldCoords = viewport.getCamera().unproject(new Vector3(x, y, 0));

                // Convert world coordinates to grid coordinates
                int gridX = (int) ((worldCoords.x - GRID_OFFSET_X) / TILE_SIZE);
                int gridY = (int) (worldCoords.y / TILE_SIZE);

                // 1. Check if the click was within the grid boundaries
                if (gridX >= 0 && gridX < GRID_WIDTH && gridY >= 0 && gridY < GRID_HEIGHT) {

                    if (isPlayerTurn && !gameOver) {
                        handlePlayerClick(gridX, gridY);
                    }

                    return true;
                }

                return false;
            }
        });
    }

    private void handlePlayerClick(int clickX, int clickY) {
        GridPosition clickedPos = new GridPosition(clickX, clickY);

        if (selectedUnit == null) {
            // Case 1: Unit not selected. Check if the player was clicked.
            if (clickX == player.gridX && clickY == player.gridY) {
                selectedUnit = player;
                logMessage(player.name + " selected.");
                calculateValidMoves();
            }
        } else {
            // Case 2: Unit selected. Check if a valid move cell was clicked.
            if (validMoves.contains(clickedPos)) {
                // This is a valid move!
                player.gridX = clickX;
                player.gridY = clickY;
                logMessage(player.name + " moves to (" + (clickX+1) + ", " + (clickY+1) + ")");
                endTurn();
                clearSelection();
            } else if (clickX == player.gridX && clickY == player.gridY) {
                // Clicked on the same selected unit again -> Deselect
                clearSelection();
            } else {
                // Clicked on an invalid cell -> Deselect
                clearSelection();
            }
        }
    }

    private void clearSelection() {
        selectedUnit = null;
        validMoves.clear();
    }

    private void calculateValidMoves() {
        validMoves.clear();
        if (selectedUnit == null) return;

        int[] dx = {1, -1, 0, 0};
        int[] dy = {0, 0, 1, -1};

        for (int i = 0; i < 4; i++) {
            int newX = selectedUnit.gridX + dx[i];
            int newY = selectedUnit.gridY + dy[i];

            boolean inBounds = newX >= 0 && newX < GRID_WIDTH && newY >= 0 && newY < GRID_HEIGHT;
            boolean notEnemy = (newX != enemy.gridX || newY != enemy.gridY);

            if (inBounds && notEnemy) {
                validMoves.add(new GridPosition(newX, newY));
            }
        }
    }


    // --- Game Logic ---

    private void movePlayer(int dx, int dy) {
        if (gameOver || !isPlayerTurn || (dx == 0 && dy == 0)) return;

        int newX = player.gridX + dx;
        int newY = player.gridY + dy;

        if (newX >= 0 && newX < GRID_WIDTH && newY >= 0 && newY < GRID_HEIGHT && (newX != enemy.gridX || newY != enemy.gridY)) {
            player.gridX = newX;
            player.gridY = newY;
            logMessage(player.name + " moves to (" + (newX+1) + ", " + (newY+1) + ")");
            endTurn();
        } else {
            logMessage("Invalid move.");
        }
    }

    private void attackEnemy() {
        if (gameOver || !isPlayerTurn) return;

        if (Math.abs(player.gridX - enemy.gridX) + Math.abs(player.gridY - enemy.gridY) == 1) {
            enemy.hp -= player.attackDamage;
            logMessage(player.name + " attacks " + enemy.name + ". Enemy HP remaining: " + enemy.hp);
            checkWinCondition();
            if (!gameOver) endTurn();
        } else {
            logMessage("Attack failed: Enemy out of reach.");
        }
    }

    private void checkWinCondition() {
        String message = "";
        String title = "GAME OVER";

        if (player.hp <= 0) {
            message = enemy.name.toUpperCase() + " WINS! " + player.name + " is defeated.";
            gameOver = true;
        } else if (enemy.hp <= 0) {
            message = player.name.toUpperCase() + " WINS! " + enemy.name + " is defeated.";
            gameOver = true;
        }

        if (gameOver) {
            logMessage(title + ": " + message);
            showGameOverDialog(title, message);
        }
    }

    private void showGameOverDialog(String title, String message) {

        gameOverDialog = new Dialog("", skin) {
            @Override
            protected void result(Object object) {
                if (object != null && object.equals("main_menu")) {
                    logMessage("Returning to main menu.");
                    game.setScreen(new MainMenuScreen(game, new GameClient()));
                }
            }
        };

        gameOverDialog.text(message);

        gameOverDialog.button("OK", "main_menu");
        gameOverDialog.setPosition(stage.getWidth() / 2, stage.getHeight() / 2);
        gameOverDialog.scaleBy(1.5f);
        gameOverDialog.getBackground().setMinWidth(400);
        gameOverDialog.getBackground().setMinHeight(200);
        gameOverDialog.show(stage);
    }

    private void enemyTurn() {
        if (gameOver) return;

        int dx = player.gridX - enemy.gridX;
        int dy = player.gridY - enemy.gridY;

        if (Math.abs(dx) + Math.abs(dy) == 1) {
            // Attack
            player.hp -= enemy.attackDamage;
            logMessage(enemy.name + " attacks " + player.name + ". Player HP remaining: " + player.hp);
            checkWinCondition();
        } else {
            // Move closer
            int moveX = 0;
            int moveY = 0;

            if (Math.abs(dx) >= Math.abs(dy) && dx != 0) {
                moveX = (dx > 0) ? 1 : -1;
            } else if (dy != 0) {
                moveY = (dy > 0) ? 1 : -1;
            }

            int newX = enemy.gridX + moveX;
            int newY = enemy.gridY + moveY;

            if (newX >= 0 && newX < GRID_WIDTH && newY >= 0 && newY < GRID_HEIGHT && (newX != player.gridX || newY != player.gridY)) {
                enemy.gridX = newX;
                enemy.gridY = newY;
                logMessage(enemy.name + " moves to (" + (newX+1) + ", " + (newY+1) + ")");
            } else {
                logMessage(enemy.name + " cannot move this turn.");
            }
        }

        if (!gameOver) endTurn();
    }

    private void endTurn() {
        isPlayerTurn = !isPlayerTurn;
        logMessage("--- Turn: " + (isPlayerTurn ? player.name : enemy.name) + " ---");

        setButtonsEnabled(isPlayerTurn);

        clearSelection();

        if (!isPlayerTurn) {
            enemyTurn();
        }
    }

    private void setButtonsEnabled(boolean enabled) {
        Touchable touchable = enabled ? Touchable.enabled : Touchable.disabled;
        btnMoveLeft.setTouchable(touchable);
        btnMoveRight.setTouchable(touchable);
        btnMoveUp.setTouchable(touchable);
        btnMoveDown.setTouchable(touchable);
        btnAttack.setTouchable(touchable);
    }

    private void updateCamera() {
        OrthographicCamera camera = (OrthographicCamera) viewport.getCamera();

        float playerWorldX = player.gridX * TILE_SIZE + TILE_SIZE / 2f + GRID_OFFSET_X;
        float playerWorldY = player.gridY * TILE_SIZE + TILE_SIZE / 2f;

        final float mapWidth = GRID_WIDTH * TILE_SIZE + GRID_OFFSET_X;
        final float mapHeight = GRID_HEIGHT * TILE_SIZE;

        float halfViewW = camera.viewportWidth * camera.zoom / 2f;
        float halfViewH = camera.viewportHeight * camera.zoom / 2f;

        float targetX = playerWorldX;
        float targetY = playerWorldY;

        targetX = Math.max(halfViewW + GRID_OFFSET_X, targetX);
        targetX = Math.min(mapWidth - halfViewW, targetX);

        targetY = Math.max(halfViewH, targetY);
        targetY = Math.min(mapHeight - halfViewH, targetY);

        if (GRID_WIDTH * TILE_SIZE < camera.viewportWidth) {
            targetX = GRID_WIDTH * TILE_SIZE / 2f + GRID_OFFSET_X;
        }
        if (mapHeight < camera.viewportHeight) {
            targetY = mapHeight / 2f;
        }

        camera.position.set(targetX, targetY, 0);
        camera.update();
    }


    // --- Screen Interface Implementation ---

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.15f, 0.15f, 0.2f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        updateCamera();

        // 1. WORLD RENDERING (Grid and Units)
        shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // A. Grid background rendering
        shapeRenderer.setColor(Color.DARK_GRAY);
        shapeRenderer.rect(GRID_OFFSET_X, 0, GRID_WIDTH * TILE_SIZE, GRID_HEIGHT * TILE_SIZE);

        // B. Highlighted cells rendering (if unit is selected)
        if (selectedUnit != null && isPlayerTurn) {
            shapeRenderer.setColor(new Color(0.2f, 0.5f, 0.8f, 0.5f));
            for (GridPosition pos : validMoves) {
                float pixelX = pos.x * TILE_SIZE + GRID_OFFSET_X;
                float pixelY = pos.y * TILE_SIZE;

                shapeRenderer.rect(pixelX + 5, pixelY + 5, TILE_SIZE - 10, TILE_SIZE - 10);
            }
        }

        // C. Unit rendering
        player.draw(shapeRenderer, TILE_SIZE, GRID_OFFSET_X);
        enemy.draw(shapeRenderer, TILE_SIZE, GRID_OFFSET_X);

        shapeRenderer.end();

        // 2. UI RENDERING (Scene2D)
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        stage.getViewport().update(width, height, true);
    }

    @Override public void show() { }
    @Override public void pause() { }
    @Override public void resume() { }
    @Override public void hide() { }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
        stage.dispose();
        skin.dispose();
    }
}
