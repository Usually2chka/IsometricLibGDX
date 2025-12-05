package project.example.Game.Entitys;
//
//import static project.example.Utils.Constants.TILE_HEIGHT;
//import static project.example.Utils.Constants.TILE_WIDTH;
//
//import com.badlogic.gdx.graphics.Texture;
//import com.badlogic.gdx.graphics.g2d.Animation;
//import com.badlogic.gdx.graphics.g2d.TextureRegion;
//import com.badlogic.gdx.math.Vector2;
//import com.badlogic.gdx.physics.box2d.Body;
//import com.badlogic.gdx.physics.box2d.BodyDef;
//import com.badlogic.gdx.physics.box2d.FixtureDef;
//import com.badlogic.gdx.physics.box2d.PolygonShape;
//import com.badlogic.gdx.physics.box2d.World;
//
//import java.awt.Rectangle;
//
//public class Unit {
//    protected int health;
//    protected int armor;
//    protected int damage;
//    protected int positionX;
//    protected int positionY;
//    protected Texture texture;
//    protected boolean isHighLight;
//    protected Animation<TextureRegion> animation;
//
//    public Unit(int health, int armor, int damage,
//                int positionX, int positionY, World world)//, Texture texture)
//    {
//        this.health = health;
//        this.armor = armor;
//        this.damage = damage;
//        this.positionX = positionX;
//        this.positionY = positionY;
//        //this.texture = texture;
//        isHighLight = false;
//
//        defineUnit(world);
//    }
//
//    protected Animation<TextureRegion> GetAnimation() {return null;} //TODO!
//    public Unit GiveDamage(Unit unit) {
//        unit.setHealth(getDamage());
//        return unit;
//    }
//
//    public int getHealth() {
//        return health;
//    }
//    public int getArmor() {
//        return armor;
//    }
//
//    public int getDamage() {
//        return damage;
//    }
//
//    public void setHealth(int damage) {
//        this.health -= damage;
//    }
//
//    private void defineUnit(World world) {
//
//        // --- 2. Определение Кинематического Тела (The Body) ---
//        BodyDef bodyDef = new BodyDef();
//        // KinematicBody: на него не действует гравитация, но он взаимодействует
//        // с другими телами. Его позицию и скорость мы контролируем вручную.
//        bodyDef.type = BodyDef.BodyType.KinematicBody;
//        bodyDef.position.set(this.positionX, this.positionY);
//
//        Body body =  world.createBody(bodyDef);
//
//        // --- 3. Определение Формы Коллайдера (The Shape) ---
//        PolygonShape squareShape = new PolygonShape();
//        // setAsBox принимает ПОЛУ-ширину и ПОЛУ-высоту
//        squareShape.setAsBox(TILE_WIDTH / 4, TILE_HEIGHT / 4);
//
//        // --- 4. Определение Физических Свойств (The Fixture) ---
//        FixtureDef fixtureDef = new FixtureDef();
//        fixtureDef.shape = squareShape;
//        // Для кинематических тел плотность обычно не играет роли, но ее можно оставить 0 или 1
//        fixtureDef.density = 0f;
//        fixtureDef.friction = 0.5f;
//        fixtureDef.restitution = 0f; // Обычно без отскока для изометрии
//
//        // 5. Прикрепление фикстуры к телу
//        body.createFixture(fixtureDef);
//
//        // 6. Освобождение ресурсов
//        squareShape.dispose();
//
//    }
//
//}

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Unit {
    public int gridX; // X position on the grid
    public int gridY; // Y position on the grid
    public int hp;
    public final int maxHp = 10;
    public final int attackDamage = 3;
    public final Color color;
    public final String name;

    public Unit(int gridX, int gridY, Color color, String name) {
        this.gridX = gridX;
        this.gridY = gridY;
        this.hp = maxHp;
        this.color = color;
        this.name = name;
    }

    public void draw(ShapeRenderer sr, int tileSize, int offsetX) {
        // Convert grid positions to pixel coordinates, adding the offset
        float pixelX = gridX * tileSize + offsetX;
        float pixelY = gridY * tileSize;

        // 1. Draw the unit body
        sr.setColor(color);
        sr.rect(pixelX + 10, pixelY + 10, tileSize - 20, tileSize - 20);

        // 2. Draw the Health Bar (Green part)
        float hpBarWidth = (float)hp / maxHp * tileSize;
        sr.setColor(Color.GREEN);
        sr.rect(pixelX, pixelY + tileSize - 10, hpBarWidth, 5);

        // 3. Draw the Health Bar background (Black part)
        sr.setColor(Color.BLACK);
        sr.rect(pixelX + hpBarWidth, pixelY + tileSize - 10, tileSize - hpBarWidth, 5);
    }
}
