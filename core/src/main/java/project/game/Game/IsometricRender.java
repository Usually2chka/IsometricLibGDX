package project.game.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.HashMap;
import java.util.Map;

public class IsometricRender {
    private static final int TILE_WIDTH = 64;
    private static final int TILE_HEIGHT = 64;

    private Map<String, TextureRegion> textureRegions = new HashMap<String, TextureRegion>();
    private Texture texture;
    private TextureRegion regions[][];

    //private TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("textures/Ground.atlas"));
    //private TextureAtlas.AtlasRegion grass = atlas.findRegion("GrassWithSpots");
    public IsometricRender()
    {
        texture = new Texture(Gdx.files.internal("Grass.png"));
        regions = TextureRegion.split(texture, texture.getWidth() / 9, texture.getHeight() / 9);

//        for (int i = 0; i < 3; i++)
//            for (int j = 0; j < 3; j++)
//                textureRegions.put("texture" + j + " " + i, regions[i][j]);

    }
    public void DrawGrow(SpriteBatch batch)
    {
        for (int row = 15; row >= 0; row--)
            for (int col = 15; col >= 0; col--)
            {
                float x = (row - col) * (TILE_WIDTH / 2f);
                float y = (row + col) * (TILE_HEIGHT / 4f);

                batch.draw(texture, x, y, TILE_WIDTH, TILE_HEIGHT);
            }
    }
}
