package project.example.UserInterface;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import project.example.Network.Entyties.Lobby;
import project.example.Network.Entyties.Player;
import project.example.Network.GameClient;
import project.example.Utils.TextureManager;

public class LobbyScreen implements Screen {
    private final Stage stage;
    private ArrayList<Player> players;
    private final GameClient client;
    private Label infoPlayersLabel;
    private Lobby lobby;

    public LobbyScreen(Lobby lobby, Game game, GameClient client) {
        this.client = client;
        this.lobby = lobby;

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        players = lobby.getPlayers();
        players.add(GameClient.player);

        Table table = new Table();
        table.setFillParent(true);
        table.defaults().pad(10);
        stage.addActor(table);

        Label title;
        if(lobby.getLobbyName() == null)
            title = new Label("Room: " + lobby.getLobbyName(), TextureManager.GetInstance().GetSkin());
        else
            title = new Label("Room: " + lobby.getLobbyName(), TextureManager.GetInstance().GetSkin());

        title.setAlignment(Align.center);
        table.add(title).colspan(2).center().padBottom(20);
        table.row();

        // === Список игроков ===
        infoPlayersLabel = new Label("Players (" + players.size() + "/" + lobby.getMaxPlayers() + "):", TextureManager.GetInstance().GetSkin());
        table.add(infoPlayersLabel).left().colspan(2);
        table.row();

        Table playerTable = new Table();
        Map<Player, Label> playerLabel = new HashMap<>();
        for (Player p : players) {
            Label playerName = new Label(p.toString(), TextureManager.GetInstance().GetSkin());
            playerTable.add(playerName).left().pad(5);
            playerTable.row();
            playerLabel.put(p, playerName);
        }
        table.add(playerTable).left().colspan(2);
        table.row();

        // === Настройки комнаты ===
        Label fallLabel = new Label("Fall blocks: " + lobby.getIsFallBlocks(), TextureManager.GetInstance().GetSkin());
        Label sizeLabel = new Label("World size: " + lobby.getSizeWorld() + "x" + lobby.getSizeWorld(), TextureManager.GetInstance().GetSkin());
        Label privateLabel = new Label("Private room:  " + lobby.getIsPrivate(), TextureManager.GetInstance().GetSkin());
        table.add(fallLabel).left().colspan(2);
        table.row();
        table.add(sizeLabel).left().colspan(2);
        table.row();
        table.add(privateLabel).left().colspan(2);
        table.row();

        // === Кнопки ===
        TextButton leaveBtn = new TextButton("Leave", TextureManager.GetInstance().GetSkin());
        TextButton startBtn = new TextButton("Start", TextureManager.GetInstance().GetSkin());
        table.add(leaveBtn).left().padTop(30);
        if (players.get(0) == GameClient.player) //host lobby
            table.add(startBtn).right().padTop(30);

        // === События ===
        leaveBtn.addListener(event -> {
            if (leaveBtn.isPressed())
            {
                //playerTable.removeActor(playerLabel.get(GameClient.player));
                //playerLabel.remove(GameClient.player);
                players.remove(GameClient.player);
                updateData();
                game.setScreen(new MultiplayerScreen(game, client));
            }

            return false;
        });
        startBtn.addListener(event -> {
            if (!startBtn.isPressed()) return false;
            System.out.println("Start game");
            return true;
        });
    }
    private void updateData()
    {
        client.leaveFromLobby(lobby);
        //infoPlayersLabel.setText("Players (" + players.size() + "/" + lobby.getMaxPlayers() + "):");
    }
    @Override public void show() {}
    @Override public void render(float delta) {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }
    @Override public void resize(int width, int height) { stage.getViewport().update(width, height, true); }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() { stage.dispose(); TextureManager.GetInstance().GetSkin().dispose(); }
}
