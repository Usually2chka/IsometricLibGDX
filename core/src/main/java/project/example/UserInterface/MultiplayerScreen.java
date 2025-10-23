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
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import project.example.Utils.TextureManager;

public class MultiplayerScreen implements Screen {
    private Stage stage;
    private Window window;
    private ScrollPane scrollPane;
    private List<String> list;
    private Game game;

    public MultiplayerScreen(Game game) {
        this.game = game;
        window = new Window("", TextureManager.GetInstance().GetSkin());
        stage = new Stage(new ScreenViewport());
        list = new List<>(TextureManager.GetInstance().GetSkin());
        scrollPane = new ScrollPane(list, TextureManager.GetInstance().GetSkin());
    }

    @Override
    public void show() {
        list.getStyle().font.getData().setScale(2.2f);
        Gdx.input.setInputProcessor(stage);


        // 1. Создаем основное окно
        window.setSize(2000,1000);
        window.setPosition(
            (Gdx.graphics.getWidth()/2) - 950,
            (Gdx.graphics.getHeight()/2) - 500
        );

        // 2. сервак должен заполнять список
        Array<String> items = new Array<>();
        items.add("player name                      " +
                  "name room                        " +
                  "private room                     " +
                  "  players");
;
        for (int i = 1; i <= 100; i++) {
            //items.add("some player "+ " some 2" + i);
        }
        // 3. Создаем List компонент
        list.setItems(items);

        // 4. Настраиваем ScrollPane
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(true, false); // Только вертикальная прокрутка

        // 5. Добавляем компоненты в окно
        window.add(scrollPane).expand().fill().pad(75);

        // 6. Добавляем кнопки управления


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
                game.setScreen(new HostScreen(game));
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
    //переделать создание лобби
    private void addNewItem() {
        Array<String> items = list.getItems();
        items.add("New element" + (items.size + 1));
        list.setItems(items);
        scrollPane.scrollTo(0, 0, 0, 0); // Автопрокрутка вниз
    }

    //переделать под подключение
    private void removeSelectedItem() {
        String selected = list.getSelected();
        if (selected != null) {
            Array<String> items = list.getItems();
            items.removeValue(selected, false);
            list.setItems(items);
        }
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
