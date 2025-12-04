package project.example.Game.Entitys;

import static project.example.Utils.Constants.TILE_HEIGHT;
import static project.example.Utils.Constants.TILE_WIDTH;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import java.awt.Rectangle;

public class Unit {
    protected int health;
    protected int armor;
    protected int damage;
    protected int positionX;
    protected int positionY;
    protected Texture texture;
    protected boolean isHighLight;
    protected Animation<TextureRegion> animation;

    public Unit(int health, int armor, int damage,
                int positionX, int positionY, World world)//, Texture texture)
    {
        this.health = health;
        this.armor = armor;
        this.damage = damage;
        this.positionX = positionX;
        this.positionY = positionY;
        //this.texture = texture;
        isHighLight = false;

        defineUnit(world);
    }

    protected Animation<TextureRegion> GetAnimation() {return null;} //TODO!
    public Unit GiveDamage(Unit unit) {
        unit.setHealth(getDamage());
        return unit;
    }

    public int getHealth() {
        return health;
    }
    public int getArmor() {
        return armor;
    }

    public int getDamage() {
        return damage;
    }

    public void setHealth(int damage) {
        this.health -= damage;
    }

    private void defineUnit(World world) {

        // --- 2. Определение Кинематического Тела (The Body) ---
        BodyDef bodyDef = new BodyDef();
        // KinematicBody: на него не действует гравитация, но он взаимодействует
        // с другими телами. Его позицию и скорость мы контролируем вручную.
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        bodyDef.position.set(this.positionX, this.positionY);

        Body body =  world.createBody(bodyDef);

        // --- 3. Определение Формы Коллайдера (The Shape) ---
        PolygonShape squareShape = new PolygonShape();
        // setAsBox принимает ПОЛУ-ширину и ПОЛУ-высоту
        squareShape.setAsBox(TILE_WIDTH / 4, TILE_HEIGHT / 4);

        // --- 4. Определение Физических Свойств (The Fixture) ---
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = squareShape;
        // Для кинематических тел плотность обычно не играет роли, но ее можно оставить 0 или 1
        fixtureDef.density = 0f;
        fixtureDef.friction = 0.5f;
        fixtureDef.restitution = 0f; // Обычно без отскока для изометрии

        // 5. Прикрепление фикстуры к телу
        body.createFixture(fixtureDef);

        // 6. Освобождение ресурсов
        squareShape.dispose();

    }

}
