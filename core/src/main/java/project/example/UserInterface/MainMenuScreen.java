package project.example.UserInterface;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import project.example.Network.GameClient;
import project.example.Utils.TextureManager;

public class MainMenuScreen implements Screen {
    private Stage stage;
    private Table table;

    private TextButton singlePlayerButton;
    private TextButton multiPlayerButton;
    private TextButton exitButton;
    private Game game;
    private GameClient client;
    public MainMenuScreen(Game game, GameClient client)
    {
        this.game = game;
        this.client = client;
        stage = new Stage(new ScreenViewport());

        Gdx.input.setInputProcessor(stage);

        table = new Table();
        table.setFillParent(true);
        stage.addActor(table);
        table.defaults();

        table.add(DefineGroupButton());
    }
    private VerticalGroup DefineGroupButton()
    {
        VerticalGroup group = new VerticalGroup();

        singlePlayerButton = new TextButton("Singleplayer", TextureManager.GetInstance().GetSkin());
        singlePlayerButton.setTransform(true);
        singlePlayerButton.scaleBy(2);
//        singlePlayerButton.setPosition((Gdx.graphics.getWidth()/2) - 10000,
//                                       (Gdx.graphics.getHeight()/2)- 50000);

        multiPlayerButton = new TextButton("Multiplayer", TextureManager.GetInstance().GetSkin());
        multiPlayerButton.setTransform(true);
        multiPlayerButton.scaleBy(2);
        //multiPlayerButton.setPosition(-200,-60);

        exitButton = new TextButton("Exit", TextureManager.GetInstance().GetSkin());
        exitButton.setTransform(true);
        exitButton.scaleBy(2);
        //exitButton.setPosition(-200,-120);

        group.addActor(singlePlayerButton);
        group.addActor(multiPlayerButton);
        group.addActor(exitButton);

        group.fill();
        group.space(60);
        group.align(Align.left); // Сделать по левому краю

        group.pack(); // Рассчитываем размер группы
        group.setPosition(20, Gdx.graphics.getHeight()/2 - group.getHeight()/2);

        return group;
    }

    @Override
    public void show() {
        singlePlayerButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new SingleplayerScreen(game));
            }
        });

        multiPlayerButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MultiplayerScreen(game, client));
            }
        });

        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLUE);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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
