package project.example.Game.Entitys;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;

import project.example.Utils.TextureManager;

public class Tile implements Disposable, Cloneable {
    protected Rectangle rectangle;
    protected TextureRegion texture;
    protected String textureName;
    protected boolean isTouched;

    public Tile(String textureName, float x, float y)
    {
        this.textureName = textureName;

        texture = TextureManager.GetInstance().GetTextureRegion(textureName);
        isTouched = false;
        rectangle = new Rectangle(x, y, texture.getRegionWidth(),
                                        texture.getRegionHeight());
    }

    public boolean isHit(float x, float y) {
        return rectangle.contains(x, y);
    }

    public void setTouched(boolean touched) {
        this.isTouched = touched;
    }

    public boolean IsTouched()
    {
        return isTouched;
    }
    public TextureRegion GetTextureRegion()
    {
        return texture;
    }

    public Rectangle GetRectangle()
    {
        return rectangle;
    }

    public float GetDistance(float x, float y)
    {
        float centerX = rectangle.x + rectangle.width/2;
        float centerY = rectangle.y + rectangle.height/2;
        return Vector2.dst(centerX, centerY, x, y);
    }

    @Override
    public Tile clone() {
        try {
            Tile clone = (Tile) super.clone();
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
    @Override
    public void dispose() {
        texture.getTexture().dispose();
    }
}
