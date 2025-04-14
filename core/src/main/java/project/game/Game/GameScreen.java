package project.game.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;

import project.game.Main;
import project.game.Utils.InputSystem;
import project.game.Utils.TextureManager;

public class GameScreen implements Screen {
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private IsometricRender render;
    private InputSystem input;
    private GestureDetector detector;
    private TextureManager manager;
    public GameScreen (SpriteBatch batch) {
        this.batch = batch;

        camera = new OrthographicCamera(Main.VIEW_WIDTH, Main.VIEW_HEIGHT);
        input = new InputSystem(camera);
        render = new IsometricRender();
        detector = new GestureDetector(input);
    }
    @Override
    public void show() {
        camera.position.set(Main.VIEW_WIDTH / 2, Main.VIEW_HEIGHT / 2, 5);
        Gdx.input.setInputProcessor(detector);
        manager.init();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        input.updateInertia();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();

        render.DrawGrow(batch);

        batch.end();
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
