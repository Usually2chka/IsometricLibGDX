package project.example.UserInterface;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.ArrayList;
import java.util.function.Consumer;

import project.example.Network.Entyties.Lobby;
import project.example.Network.Entyties.Player;
import project.example.Network.GameClient;
import project.example.Utils.TextureManager;

public class LobbyScreen implements Screen {

    private final Stage stage;
    private final GameClient client;
    private final Game game;
    private Lobby lobby;

    private ArrayList<Player> players;

    // UI элементы, которые будем обновлять
    private Label infoPlayersLabel;
    private Table playerTable;
    private Label fallLabel;
    private Label sizeLabel;
    private Label privateLabel;

    // Кнопки и контейнер для них
    private TextButton leaveBtn;
    private TextButton startBtn;
    private Table buttonTable;

    private Table rootTable;

    private Consumer<Array<Lobby>> lobbyConsumer;

    public LobbyScreen(Lobby lobby, Game game, GameClient client) {
        this.client = client;
        this.lobby = lobby;
        this.game = game;

        this.players = new ArrayList<>(lobby.getPlayers());

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        buildUI();
    }

    private void buildUI() {
        rootTable = new Table();
        rootTable.setFillParent(true);
        rootTable.defaults().pad(10);
        stage.addActor(rootTable);

        // === Заголовок ===
        Label title = new Label("Room: " + lobby.getLobbyName(), TextureManager.GetInstance().GetSkin());
        title.setAlignment(Align.center);
        rootTable.add(title).colspan(2).center().padBottom(20);
        rootTable.row();

        // === Список игроков ===
        infoPlayersLabel = new Label("", TextureManager.GetInstance().GetSkin());
        rootTable.add(infoPlayersLabel).left().colspan(2);
        rootTable.row();

        playerTable = new Table();
        rootTable.add(playerTable).left().colspan(2);
        rootTable.row();

        // === Настройки комнаты ===
        fallLabel = new Label("", TextureManager.GetInstance().GetSkin());
        sizeLabel = new Label("", TextureManager.GetInstance().GetSkin());
        privateLabel = new Label("", TextureManager.GetInstance().GetSkin());

        rootTable.add(fallLabel).left().colspan(2);
        rootTable.row();
        rootTable.add(sizeLabel).left().colspan(2);
        rootTable.row();
        rootTable.add(privateLabel).left().colspan(2);
        rootTable.row();

        // === Кнопки ===
        leaveBtn = new TextButton("Leave", TextureManager.GetInstance().GetSkin());
        startBtn = new TextButton("Start", TextureManager.GetInstance().GetSkin());

        // Слушатели кнопок
        leaveBtn.addListener(event -> {
            if (leaveBtn.isPressed()) {
                client.leaveFromLobby(lobby);
                game.setScreen(new MultiplayerScreen(game, client));
            }
            return false;
        });

        startBtn.addListener(event -> {
            if (!startBtn.isPressed()) return false;
            // TODO: отправить команду старта на сервер
            System.out.println("Start game pressed by host");
            return true;
        });

        // Добавляем контейнер кнопок — его будем перестраивать в updateLobbyUI()
        buttonTable = new Table();
        rootTable.add(buttonTable).colspan(2).padTop(30);
        rootTable.row();

        // Первичное заполнение UI
        updateLobbyUI();
    }

    private void updateLobbyUI() {
        // Обновляем список игроков/лейбл
        infoPlayersLabel.setText("Players (" + players.size() + "/" + lobby.getMaxPlayers() + "):");

        playerTable.clear();
        for (Player p : players) {
            Label playerName = new Label(p.toString(), TextureManager.GetInstance().GetSkin());
            if (p.id == lobby.hostPlayer.id) {
                playerName.setColor(Color.GOLD);
                playerName.setText(p.toString() + " <-- leaderRoom");
            }
            playerTable.add(playerName).left().pad(5);
            playerTable.row();
        }

        // Обновляем настройки
        fallLabel.setText("Fall blocks: " + lobby.getIsFallBlocks());
        sizeLabel.setText("World size: " + lobby.getSizeWorld() + "x" + lobby.getSizeWorld());
        privateLabel.setText("Private room: " + lobby.getIsPrivate());

        // Обновляем панель кнопок: Leave всегда слева, Start — только если текущий игрок хост
        buttonTable.clear();
        buttonTable.add(leaveBtn).left();
        boolean isHost = !players.isEmpty() && (players.get(0).getId() == GameClient.player.id);
        if (isHost) {
            buttonTable.add(startBtn).right();
        }
    }

    @Override
    public void show() {
        lobbyConsumer = updatedLobbies -> {
            for (Lobby l : updatedLobbies) {
                if (l.getId() == lobby.getId()) {
                    this.lobby = l;
                    this.players = new ArrayList<>(l.getPlayers());
                    Gdx.app.postRunnable(this::updateLobbyUI);
                    break;
                }
            }
        };
        client.addLobbyListener(lobbyConsumer);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override public void pause() {}
    @Override public void resume() {}

    @Override
    public void hide() {
        client.removeLobbyListener(lobbyConsumer);
    }

    @Override
    public void dispose() {
        stage.dispose();
        TextureManager.GetInstance().GetSkin().dispose();
    }
}

