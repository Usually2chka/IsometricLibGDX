package project.game.Game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import project.game.Utils.TextureManager;

public class IsometricRender {
    private static final int TILE_WIDTH = 64;
    private static final int TILE_HEIGHT = 64;
    private TextureRegion region;
    public IsometricRender()
    {
        region = TextureManager.
            GetInstance().
            GetTextureRegion("WinterGrassWithForest");
    }
    public void DrawGrow(SpriteBatch batch)
    {
        for (int row = 15; row >= 0; row--)
            for (int col = 15; col >= 0; col--)
            {
                float x = (row - col) * (TILE_WIDTH / 2f);
                float y = (row + col) * (TILE_HEIGHT / 4f);

                batch.draw(region, x, y, TILE_WIDTH, TILE_HEIGHT);
            }
    }
}
