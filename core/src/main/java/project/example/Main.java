package project.example;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.io.IOException;

import project.example.Network.GameClient;
import project.example.UserInterface.LoadingScreen;
import project.example.Utils.TextureManager;

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
        this.setScreen(new LoadingScreen(spriteBatch, this));
        //this.setScreen(new GameScreen(spriteBatch));
    }
}
