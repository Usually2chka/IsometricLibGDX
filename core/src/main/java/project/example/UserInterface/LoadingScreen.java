package project.example.UserInterface;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.io.IOException;

import project.example.Network.GameClient;
import project.example.Utils.TextureManager;

public class LoadingScreen implements Screen {
    private Game game;
    private SpriteBatch spriteBatch;
    GameClient client;
    public LoadingScreen(SpriteBatch spriteBatch, Game game)
    {
        this.spriteBatch = spriteBatch;
        this.game = game;

    }
    @Override
    public void show() {
        client = new GameClient();
    }

    @Override
    public void render(float delta) {
        if (TextureManager.GetInstance().Update()) {
            TextureManager.GetInstance().FinishLoading();
            game.setScreen(new MainMenuScreen(game, client));
            //game.setScreen(new GameScreen(spriteBatch, game));
        }
        else {
            //progress bar
        }
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

    }
}
