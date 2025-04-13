package project.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import project.game.Game.GameScreen;
import project.game.Game.IsometricRender;
import project.game.UserInterface.ClientScreen;
import project.game.UserInterface.HostScreen;
import project.game.UserInterface.MainMenuScreen;

public class Main extends Game {
    public static final int VIEW_WIDTH = 1000;
    public static final int VIEW_HEIGHT = 500;
    public static final int PIXEL_PER_METER = 100;

    private SpriteBatch spriteBatch;
    @Override
    public void create() {
        spriteBatch = new SpriteBatch();
        //this.setScreen(new MainMenuScreen(this));
        this.setScreen(new GameScreen(spriteBatch));
    }
}
