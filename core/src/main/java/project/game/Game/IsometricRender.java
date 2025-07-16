package project.game.Game;

import static project.game.Utils.Constants.TILE_HEIGHT;
import static project.game.Utils.Constants.TILE_WIDTH;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;

import project.game.Game.Entitys.Block;

public class IsometricRender {
    private Block[][] blocks;
    private World world;
    public IsometricRender(World world)
    {
        this.world = world;
    }
    public void DrawGrow(SpriteBatch batch)
    {
        Block[][] ground = new Block[16][16];

        for (int row = 15; row >= 0; row--)
            for (int col = 15; col >= 0; col--)
            {
                float x = (row - col) * (TILE_WIDTH / 2f);
                float y = (row + col) * (TILE_HEIGHT / 4f);

                ground[row][col] = new Block(world, "WinterGrassWithForest", x, y);
                (ground[row][col]).RenderBlock(batch);
            }
        blocks = ground;
    }
}
