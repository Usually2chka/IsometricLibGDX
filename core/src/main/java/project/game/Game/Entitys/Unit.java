package project.game.Game.Entitys;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Unit {
    protected short health;
    protected short armor;
    protected short damage;
    protected short positionX;
    protected short positionY;
    protected Texture texture;
    protected boolean isHighLight;
    protected Animation<TextureRegion> animation;

    public Unit(short health, short armor, short damage,
                short positionX, short positionY, Texture texture)
    {
        this.health = health;
        this.armor = armor;
        this.damage = damage;
        this.positionX = positionX;
        this.positionY = positionY;
        this.texture = texture;
        isHighLight = false;
    }

    protected Animation<TextureRegion> GetAnimation() {return null;} //TODO!
    public Unit GiveDamage(Unit unit) {
        unit.setHealth(getDamage());
        return unit;
    }

    public short getHealth() {
        return health;
    }
    public short getArmor() {
        return armor;
    }

    public short getDamage() {
        return damage;
    }

    public void setHealth(short damage) {
        this.health -= damage;
    }
}
