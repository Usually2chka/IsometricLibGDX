package project.example.Game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import project.example.Main;
import project.example.Utils.InputSystem;

public class GameScreen implements Screen {
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private IsometricRender render;
    private InputSystem input;
    private GestureDetector detector;
    private World world;

    private TileMap tileMap; // dont delete this

    public GameScreen (SpriteBatch batch, Game game) {
        this.batch = batch;


        world = new World(new Vector2(0, 0), true);
        camera = new OrthographicCamera(Main.VIEW_WIDTH, Main.VIEW_HEIGHT);
        render = new IsometricRender();
        input = new InputSystem(camera);
        detector = new GestureDetector(input);
        tileMap = new TileMap();
    }
    @Override
    public void show() {
        camera.position.set(Main.VIEW_WIDTH / 2, Main.VIEW_HEIGHT / 2, 5);
        Gdx.input.setInputProcessor(detector);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();

        input.updateInertia();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();


        world.step(1/60f, 6, 2);
        //render.Debuging();
        render.Render(batch);

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
