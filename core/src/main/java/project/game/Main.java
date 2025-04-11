package project.game;

import com.badlogic.gdx.Game;

import project.game.UserInterface.ClientScreen;
import project.game.UserInterface.HostScreen;
import project.game.UserInterface.MainMenuScreen;

public class Main extends Game {

    @Override
    public void create() {
        //this.setScreen(new MainMenuScreen(this));
        this.setScreen(new HostScreen());
    }
}
