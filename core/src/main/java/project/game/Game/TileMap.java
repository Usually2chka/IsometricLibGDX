package project.game.Game;

import static project.game.Utils.Constants.TILE_HEIGHT;
import static project.game.Utils.Constants.TILE_WIDTH;

import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import project.game.Game.Entitys.Tile;

public class TileMap extends ClickListener {
    private static Tile[][] blocks;
    public TileMap()
    {
        FillMap();
    }
    private void FillMap()
    {
        blocks = new Tile[16][16];

        for (int row = 15; row >= 0; row--)
            for (int col = 15; col >= 0; col--)
            {
                float x = (row - col) * (TILE_WIDTH / 2f);
                float y = (row + col) * (TILE_HEIGHT / 4f);

                blocks[row][col] = new Tile("Grass", x, y);
            }
    }
    public static Tile[][] GetMap()
    {
        return blocks.clone();
    }
}
