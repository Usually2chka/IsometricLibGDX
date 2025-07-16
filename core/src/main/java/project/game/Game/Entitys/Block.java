package project.game.Game.Entitys;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;

import project.game.Utils.TextureManager;

public class Block implements Disposable {
    protected Rectangle rectangle;
    protected World world;
    protected TextureRegion texture;
    protected String textureName;
    protected Sprite sprite;

    public Block(World world, String textureName, float x, float y)
{
        this.world = world;
        this.textureName = textureName;

        texture = TextureManager.GetInstance().GetTextureRegion(textureName);
        sprite = new Sprite(texture);
        rectangle = new Rectangle(x, y, texture.getRegionWidth(),
                                              texture.getRegionHeight());
        DefineBlock(x, y);
    }

    protected void DefineBlock(float x, float y)
    {
        sprite.setBounds(x, y, texture.getRegionWidth(),
                                     texture.getRegionHeight());
    }

    public void RenderBlock(SpriteBatch batch)
    {
        sprite.draw(batch);
    }

    public boolean isClicked(float touchX, float touchY) {
        return rectangle.contains(touchX, touchY);
    }

    @Override
    public void dispose() {
        texture.getTexture().dispose();
    }
}
