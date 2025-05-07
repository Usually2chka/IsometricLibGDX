package project.game.Utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TextureManager {
    private static AssetManager manager = new AssetManager();
    private static TextureAtlas texture = new TextureAtlas();

    private TextureManager() { }

    public static boolean Update()
    {
        return manager.update();
    }

    public static void init()
    {
        manager.load(Constants.PATH_TO_ATLAS, TextureAtlas.class);
        manager.load(Constants.PATH_TO_TEXTURES, TextureAtlas.class);

        if(manager.update())
        {
            texture = manager.get(Constants.PATH_TO_ATLAS, TextureAtlas.class);
        }
    }

    public static TextureRegion GetRegion(String name)
    {
        TextureRegion region = texture.findRegion("GrassWithSpots"); // null object
        if (region == null)
            Gdx.app.log("Null", "object");
        return region;
    }
}
