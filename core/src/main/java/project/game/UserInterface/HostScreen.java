package project.game.UserInterface;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.io.IOException;

import Network.Entity.GameServer;

public class HostScreen implements Screen {
    private Stage stage = new Stage(new ScreenViewport());;
    private GameServer host;
    private Label tokenLabel;
    private Table table = new Table();
    private Group group = new Group();
    public HostScreen() {
        Gdx.input.setInputProcessor(stage);
        table.setFillParent(true);
        stage.addActor(table);

        try {
            host = new GameServer();
            //String token = host.getGameToken();

            // Показываем токен игроку (например, для копирования)
            //tokenLabel = new Label("Token for connect " + token, new Label.LabelStyle(new BitmapFont(), Color.WHITE));
            //group.addActor(tokenLabel);
            //group.setScale(200f, 200f);
            table.add(tokenLabel);
        } catch (IOException e) {
            e.printStackTrace();
        }

        stage.addActor(table);
        stage.setDebugAll(true);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);
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
