package project.example.UserInterface;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.function.Consumer;

import project.example.Network.Entyties.Lobby;
import project.example.Network.Entyties.Player;
import project.example.Network.GameClient;
import project.example.Utils.TextureManager;

public class MultiplayerScreen implements Screen {
    private Consumer<Array<Lobby>> lobbyConsumer;
    private Stage stage;
    private Window window;
    private ScrollPane scrollPane;
    private List<Lobby> list;
    private Game game;
    private GameClient client;

    public MultiplayerScreen(Game game, GameClient client) {
        this.game = game;
        window = new Window("", TextureManager.GetInstance().GetSkin());
        stage = new Stage(new ScreenViewport());
        list = new List<Lobby>(TextureManager.GetInstance().GetSkin());
        scrollPane = new ScrollPane(list, TextureManager.GetInstance().GetSkin());
        this.client = client;
    }

    @Override
    public void show() {
        list.getStyle().font.getData().setScale(3f);
        Gdx.input.setInputProcessor(stage);


        // Создаем основное окно
        window.setSize(2000,1000);
        window.setPosition(
            (Gdx.graphics.getWidth()/2) - 950,
            (Gdx.graphics.getHeight()/2) - 500
        );
        // Создаем List компонент
        list.setItems(client.getLobbies());
            // Настраиваем ScrollPane
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(true, false); // Только вертикальная прокрутка

        // Добавляем компоненты в окно
        window.add(scrollPane).expand().fill().pad(75);

        // Добавляем кнопки управления
        HorizontalGroup group = new HorizontalGroup();
        TextButton connectButton = new TextButton("Connect", TextureManager.GetInstance().GetSkin());
        connectButton.setScale(1.3f);
        connectButton.setTransform(true);
        connectButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Lobby selectLobby = list.getSelected();
                client.connectToLobby(selectLobby, allowed -> {
                    if (client.state == GameClient.ClientState.IN_LOBBY)
                    {
                        client.state = GameClient.ClientState.ALLOWED;
                        //Player.inCurrentLobby = selectLobby;
                        game.setScreen(new LobbyScreen(selectLobby, game, client));
                    }
                    else
                    {
                        Dialog dialog = new Dialog("", TextureManager.GetInstance().GetSkin()) {
                            @Override
                            protected void result(Object object) {
                                this.hide();
                            }
                        };

                        // Текст сообщения
                        Label message = new Label("Lobby is full", TextureManager.GetInstance().GetSkin());
                        message.setAlignment(Align.center);
                        dialog.getContentTable().pad(12f);
                        dialog.getContentTable().add(message).width(300f).padBottom(8f);

                        dialog.button("OK", true);

                        // запретить закрытие кликом вне диалога
                        dialog.setModal(true);
                        dialog.setMovable(false);

                        dialog.show(stage);

                        dialog.setSize(500, 200);
                        dialog.setPosition(
                            stage.getWidth() / 2f - dialog.getWidth() / 2f,
                            stage.getHeight() / 2f - dialog.getHeight() / 2f
                        );

                        dialog.setColor(1, 1, 1, 1);
                        dialog.setBackground(TextureManager.GetInstance().GetSkin().newDrawable("white", Color.DARK_GRAY));
                    }
                });
            }
        });

        TextButton createLobbyButton = new TextButton("Create lobby", TextureManager.GetInstance().GetSkin());
        createLobbyButton.setScale(1.3f);
        createLobbyButton.setTransform(true);
        createLobbyButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new CreateLobbyScreen(game, client));
            }
        });

        TextButton buttonToBack = new TextButton("Back", TextureManager.GetInstance().GetSkin());
        buttonToBack.setScale(1.3f);
        buttonToBack.setTransform(true);
        buttonToBack.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                list.getStyle().font.getData().setScale(1); // Возвращаем изначальный размер, чтобы при переходе на экран не ломался шрифт
                game.setScreen(new MainMenuScreen(game, client));
            }
        });


        window.row();
        DefineListeners();
        group.addActor(buttonToBack);

        group.addActor(createLobbyButton);
        group.addActor(connectButton);
        group.space(550);
        group.padRight(50);
        window.add(group);
        stage.addActor(window);

        lobbyConsumer = this::updateLobbies;
        client.addLobbyListener(lobbyConsumer);//lobbies -> list.setItems(lobbies));

//        client.updateClient(changedLobbies -> {
//            list.setItems(changedLobbies);
//        });
    }
    private void DefineListeners()
    {

    }

    private void updateLobbies(Array<Lobby> lobbies) {
        list.setItems(lobbies);
    }

    @Override
    public void render(float delta)
    {
        ScreenUtils.clear(Color.BLUE);
        stage.act(delta);

        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        client.removeLobbyListener(lobbyConsumer);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
