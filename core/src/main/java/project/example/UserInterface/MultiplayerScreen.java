package project.example.UserInterface;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import project.example.Network.Entyties.Lobby;
import project.example.Network.GameClient;
import project.example.Utils.TextureManager;

public class MultiplayerScreen implements Screen {
    private Stage stage;
    private Window window;
    private ScrollPane scrollPane;
    private List<String> list;
    private Game game;
    private GameClient client;

    public MultiplayerScreen(Game game, GameClient client) {
        this.game = game;
        window = new Window("", TextureManager.GetInstance().GetSkin());
        stage = new Stage(new ScreenViewport());
        list = new List(TextureManager.GetInstance().GetSkin());
        scrollPane = new ScrollPane(list, TextureManager.GetInstance().GetSkin());
        this.client = client;
    }

    @Override
    public void show() {
        list.getStyle().font.getData().setScale(2.2f);
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

        TextButton connectButton = new TextButton("Connect", TextureManager.GetInstance().GetSkin());
        connectButton.setTransform(true);
        connectButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new ClientScreen());
            }
        });

        TextButton createLobbyButton = new TextButton("Create lobby", TextureManager.GetInstance().GetSkin());
        createLobbyButton.setTransform(true);
        createLobbyButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new HostScreen(game, client));
            }
        });

        window.row();
        DefineListeners();
        window.add(createLobbyButton).padRight(10);
        window.add(connectButton);

        stage.addActor(window);
    }
    private void DefineListeners()
    {

    }


    @Override
    public void render(float delta)
    {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
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

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
