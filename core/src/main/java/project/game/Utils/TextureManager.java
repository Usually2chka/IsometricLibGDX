package project.game.Utils;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;

public class TextureManager implements Disposable {
    private static TextureManager instance;
    private AssetManager manager;
    private TextureAtlas atlas;

    private TextureManager()
    {
        manager = new AssetManager();
    }

    public static TextureManager GetInstance()
    {
        if(instance == null)
            instance = new TextureManager();
        return instance;
    }

    public void Init()
    {
        manager.load(Constants.PATH_TO_ATLAS, TextureAtlas.class);
    }

    public TextureRegion GetTextureRegion(String regionName) {
        if (atlas == null) {
            throw new RuntimeException("Atlas not loaded yet!");
        }
        TextureRegion region = atlas.findRegion(regionName);

        if (region == null) {
            throw new RuntimeException("Region '" + regionName +
                "' not found in atlas!");
        }
        return region;
    }

    public boolean Update()
    {
        return manager.update();
    }

    public void FinishLoading() {
        manager.finishLoading();
        atlas = manager.get(Constants.PATH_TO_ATLAS, TextureAtlas.class);
    }

    @Override
    public void dispose() {
        atlas.dispose();
        manager.dispose();
    }
}
