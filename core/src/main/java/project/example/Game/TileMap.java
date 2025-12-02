package project.example.Game;

import static project.example.Utils.Constants.TILE_HEIGHT;
import static project.example.Utils.Constants.TILE_WIDTH;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;

import project.example.Game.Entitys.Tile;
import project.example.Game.Entitys.Unit;
import project.example.Utils.Constants;

public class TileMap extends ClickListener {
    private World world;
    private static Tile[][] blocks;
    public TileMap(World world)
    {
        this.world = world;

        FillMap();
    }

    public static Tile[][] GetMap()
    {
        return blocks.clone();
    }

    private void FillMap()
    {
        blocks = new Tile[16][16];

        for (int row = 15; row >= 0; row--)
            for (int col = 15; col >= 0; col--)
            {
                float x = (row - col) * (TILE_WIDTH / 2f);
                float y = (row + col) * (TILE_HEIGHT / 4f);

                blocks[row][col] = new Tile("Grass", x, y, world);
            }
    }
}
