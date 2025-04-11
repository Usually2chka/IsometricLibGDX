package project.game.UserInterface;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.io.IOException;

import Network.Entity.ClientPlayer;

public class ClientScreen implements Screen {
    private Stage stage = new Stage(new ScreenViewport());
    private TextField ipField, tokenField;
    private Skin skin = new Skin(Gdx.files.internal("ui/font/mnml-font.json"));
    private Table table = new Table();
    public ClientScreen() {
        Gdx.input.setInputProcessor(stage);
        table.setFillParent(true);

        ipField = new TextField("IP HOST", new TextField.TextFieldStyle(new BitmapFont(), Color.WHITE, null, null,null));
        table.add(ipField).width(400).height(50);
        table.row();
        tokenField = new TextField("TOKEN", new TextField.TextFieldStyle(new BitmapFont(), Color.WHITE, null, null,null));
        table.add(tokenField).width(400).height(50);
        table.top().padTop(100);

        TextButton connectButton = new TextButton("Connect", new TextButton.TextButtonStyle(null, null, null, new BitmapFont()));
        connectButton.setScale(3);
        connectButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                try {
                    new ClientPlayer().connect(
                        ipField.getText(),
                        tokenField.getText(),
                        "Player1" // Можно добавить поле для имени
                    );
                }
                catch (IOException e) {
                    Gdx.app.error("Network", "Ошибка подключения", e);
                }
            }
        });
        table.add(connectButton);

        stage.setKeyboardFocus(tokenField);
        stage.setKeyboardFocus(ipField);
        stage.addActor(table);

        stage.setDebugAll(true);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.GRAY);
        stage.act(Gdx.graphics.getDeltaTime());
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
