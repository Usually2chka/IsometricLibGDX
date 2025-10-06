package project.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.io.IOException;

import Network.GameClient;
import project.game.Utils.TextureManager;

public class Main extends Game {
    public static final int VIEW_WIDTH = 1500;
    public static final int VIEW_HEIGHT = 1000;
    public static final int PIXEL_PER_METER = 100;
    private SpriteBatch spriteBatch;
    @Override
    public void create() {
        spriteBatch = new SpriteBatch();
        //this.setScreen(new MainMenuScreen(this));
        TextureManager.GetInstance().Init();
        try {
            GameClient player = new GameClient();
            player.sendMessage("");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //this.setScreen(new LoadingScreen(spriteBatch, this));
        //this.setScreen(new GameScreen(spriteBatch));
    }
}
