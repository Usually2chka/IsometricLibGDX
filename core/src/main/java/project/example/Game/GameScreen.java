package project.example.Game;

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
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

import project.example.Game.Entitys.Unit;
import project.example.Network.Entyties.Lobby;
import project.example.Network.Entyties.Player;
import project.example.Network.GameClient;
import project.example.Network.Packets.GameStatePacket;
import project.example.UserInterface.MainMenuScreen;
import project.example.Utils.TextureManager;

public class GameScreen implements Screen {
    private GameClient client;
    private final Game game;
    private final SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private Viewport viewport;

    // Scene2D elements
    private Stage stage;
    private Skin skin = TextureManager.GetInstance().GetSkin();;
    private TextArea gameLogArea;
    private Dialog gameOverDialog;
    private HashMap<Integer, Unit> enemies;
    private Unit unit;
    private int[] queueTurns;
    private int currentTurns;
    private boolean isPlayerTurn;
    //private Unit enemy;
    private boolean gameOver = false;
    private Unit selectedUnit = null;
    private List<GridPosition> validMoves = new ArrayList<>();

    // Grid Parameters
    private int TILE_SIZE = 60;
    private int GRID_OFFSET_X = 200;
    private int GRID_WIDTH;
    private int GRID_HEIGHT;
    private Lobby lobby;

    private int PLAYER_START_X;
    private int PLAYER_START_Y;
    private ArrayList<int[]> enemiesPosition;
    private Consumer<GameStatePacket> gameState;
    private boolean isMapLoaded = false;
    private int quantityPacket = 0;
    private HashMap<Integer, int[]> coord;
    private GameStatePacket packet = new GameStatePacket();

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

    public GameScreen(Game game, SpriteBatch batch, GameClient client, Lobby lobby) {
        this.client = client;
        this.game = game;
        this.batch = batch;
        this.lobby = lobby;
        GRID_HEIGHT = GRID_WIDTH = lobby.getSizeWorld();
        shapeRenderer = new ShapeRenderer();
        viewport = new ScreenViewport();
        stage = new Stage(new ScreenViewport(), batch);
        Gdx.input.setInputProcessor(stage);

        client.gameStateListener(gameState -> {
            Gdx.app.postRunnable(() -> initGame(gameState));
        });
    }

    private void processedNewGameState(GameStatePacket gameState) {
        coord = gameState.playerIdToCoordinate;
        currentTurns = gameState.currentTurns;

        if (currentTurns == GameClient.player.id)
            isPlayerTurn = true;

        unit.gridX = coord.get(GameClient.player.id)[0];
        unit.gridY = coord.get(GameClient.player.id)[1];
        for (Player p : lobby.getPlayers())
            if (p.getId() != GameClient.player.getId())
            {
                enemies.get(p.getId()).gridX = gameState.playerIdToCoordinate.get(p.getId())[0];
                enemies.get(p.getId()).gridY = gameState.playerIdToCoordinate.get(p.getId())[1];
            }
        //enemies.get(gameState.enemyPlayer.getId()).hp =
    }

    private void initGame(GameStatePacket gameState) {
        if (quantityPacket != 0) processedNewGameState(gameState);
        quantityPacket++;
        currentTurns = gameState.currentTurns;
        if (currentTurns == GameClient.player.id)
            isPlayerTurn = true;

        enemies = new HashMap<>(); // Инициализация списка игроков
        coord = gameState.playerIdToCoordinate;
        unit = new Unit(coord.get(GameClient.player.id)[0], coord.get(GameClient.player.id)[1], Color.BLUE, GameClient.player.getName());


        for (Player p : lobby.getPlayers()) {
            if (p.getId() != GameClient.player.getId())
                enemies.put(p.getId(), new Unit(coord.get(p.id)[0], coord.get(p.id)[1], Color.RED, p.getName()));
        }

        setupSkinAndButtons();
        setupLayout();
        setupWorldInputListener();

        isMapLoaded = true;
        logMessage("Game started. Turn: " + unit.name);
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
        rootTable.add(gameLogArea).width(400).height(400).pad(10).top().right().row();

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
            // Case 1: Unit not selected. Check if the unit was clicked.
            if (clickX == unit.gridX && clickY == unit.gridY) {
                selectedUnit = unit;
                logMessage(unit.name + " selected.");
                calculateValidMoves();
            }
        } else {
            // Case 2: Unit selected. Check if a valid move cell was clicked.
            if (validMoves.contains(clickedPos)) {
                // This is a valid move!
                unit.gridX = clickX;
                unit.gridY = clickY;
                logMessage(unit.name + " moves to (" + (clickX+1) + ", " + (clickY+1) + ")");
                endTurn();
                clearSelection();
            } else if (clickX == unit.gridX && clickY == unit.gridY) {
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
            boolean notEnemy = findEnemyInTile(newX, newY) == null;

            if (inBounds && notEnemy) {
                validMoves.add(new GridPosition(newX, newY));
            }
        }
    }


    // --- Game Logic ---

    private void movePlayer(int dx, int dy) {
        if (gameOver || !isPlayerTurn || (dx == 0 && dy == 0)) return;

        int newX = unit.gridX + dx;
        int newY = unit.gridY + dy;

        if (newX >= 0 && newX < GRID_WIDTH && newY >= 0 && newY < GRID_HEIGHT && (findEnemyInTile(newX, newY) == null)) {
            packet.lobbyId = lobby.id;
            packet.playerTurned.positionX = unit.gridX;
            packet.playerTurned.positionY = unit.gridY;
            unit.gridX = newX;
            unit.gridY = newY;
            logMessage(unit.name + " moves to (" + (newX+1) + ", " + (newY+1) + ")");
            endTurn();
        } else {
            logMessage("Invalid move.");
        }
    }

    private Unit findEnemyInTile(int nextX, int nextY) {
        Unit enemy = null;
        for (Unit u : enemies.values())
            if (u.gridX == nextX && u.gridY == nextY)
                return enemy = u;
        return enemy;
    }
    private Unit findNearEnemy() {
        Unit enemy = null;
        for (Unit e : enemies.values())
            if (Math.abs(unit.gridX - e.gridX) + Math.abs(unit.gridY - e.gridY) == 1)
                return enemy = e;
        return enemy;
    }
    private void attackEnemy() {
        if (gameOver || !isPlayerTurn) return;

        if (findNearEnemy() != null) {
            findNearEnemy().hp -= unit.attackDamage;
            logMessage(unit.name + " attacks " + findNearEnemy().name + ". Enemy HP remaining: " + findNearEnemy().hp);
            checkWinCondition();
            if (!gameOver) endTurn();
        } else {
            logMessage("Attack failed: Enemy out of reach.");
        }
    }

    private void checkWinCondition() {
        String message = "";
        String title = "GAME OVER";

        //winPacket
//        if (unit.hp <= 0) {
//            message = enemy.name.toUpperCase() + " WINS! " + unit.name + " is defeated.";
//            gameOver = true;
//        } else if (enemy.hp <= 0) {
//            message = unit.name.toUpperCase() + " WINS! " + enemy.name + " is defeated.";
//            gameOver = true;
//        }

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
                    game.setScreen(new MainMenuScreen(game, client));
                }
            }
        };

        gameOverDialog.text(message);

        gameOverDialog.button("OK", "main_menu");
        gameOverDialog.setPosition(stage.getWidth() / 2, stage.getHeight() / 2);
        //gameOverDialog.scaleBy(1.5f);
        gameOverDialog.getBackground().setMinWidth(400);
        gameOverDialog.getBackground().setMinHeight(200);
        gameOverDialog.show(stage);
    }

    private void endTurn() {
        isPlayerTurn = !isPlayerTurn;

        //logMessage("--- Turn: " + (isPlayerTurn ? unit.name : enemies.get(currentTurns)) + " ---");

        setButtonsEnabled(isPlayerTurn);

        clearSelection();
        packet.playerTurned.id = GameClient.player.id;
        packet.playerTurned.isTurned = isPlayerTurn;
        client.sendGameState(packet);
//        if (!isPlayerTurn) {
//            for (int i = currentTurns; i < currentTurns.length; i++)
//            {
//                if (i == currentTurns.length-1)
//                    currentTurns = currentTurns[0];
//                else
//                    currentTurns = currentTurns[i];
//            }
//        }
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

        float playerWorldX = unit.gridX * TILE_SIZE + TILE_SIZE / 2f + GRID_OFFSET_X;
        float playerWorldY = unit.gridY * TILE_SIZE + TILE_SIZE / 2f;

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

        if (!isMapLoaded) {
            stage.act(delta);
            stage.draw();
            return;
        }

        updateCamera();

        // 1. WORLD RENDERING (Grid and Units)
        shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // A. Grid background rendering
        shapeRenderer.setColor(Color.DARK_GRAY);
        // Убедитесь, что GRID_WIDTH/HEIGHT инициализированы до этого момента
        shapeRenderer.rect(GRID_OFFSET_X, 0, GRID_WIDTH * TILE_SIZE, GRID_HEIGHT * TILE_SIZE);

        // ... отрисовка выделения (без изменений) ...

        // C. Unit rendering
        if (unit != null) unit.draw(shapeRenderer, TILE_SIZE, GRID_OFFSET_X);
        if (!enemies.isEmpty()) for (Unit u : enemies.values()) u.draw(shapeRenderer, TILE_SIZE, GRID_OFFSET_X);

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
