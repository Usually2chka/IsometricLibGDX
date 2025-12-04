package project.example.Game.Entitys;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;

import project.example.Utils.TextureManager;

public class Tile implements Disposable, Cloneable {
    protected Rectangle rectangle;
    protected TextureRegion texture;
    protected String textureName;
    protected boolean isTouched;
    public Body body;

    public Tile(String textureName, float x, float y, World world)
    {
        this.textureName = textureName;

        texture = TextureManager.GetInstance().GetTextureRegion(textureName);
        isTouched = false;
        rectangle = new Rectangle(x, y, texture.getRegionWidth(),
                                        texture.getRegionHeight());
        createBox2DBody(world);
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
    private void createBox2DBody(World world) {
        // 1. Определяем параметры тела
        BodyDef bodyDef = new BodyDef();
        // Тайл статичен (не двигается под действием физики)
        bodyDef.type = BodyDef.BodyType.StaticBody;
        // Позиция в метрах Box2D (центр прямоугольника)
        bodyDef.position.set(
            (rectangle.x + rectangle.width / 2),
            (rectangle.y + rectangle.height / 2)+16
        );

        // 2. Создаем тело в мире
        this.body = world.createBody(bodyDef);

        //body.setTransform(body.getPosition(), (float) Math.toRadians(65));

        // 3. Создаем форму-прямоугольник (размеры задаются как половины ширины/высоты)
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(
            (rectangle.width / 2)-24,
            (rectangle.height / 2)-24
        );

        // 4. Создаем фикстуру и прикрепляем к телу
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.0f; // Нулевая плотность для статичного тела
        fixtureDef.friction = 0.4f; // Можно настроить трение
        fixtureDef.restitution = 0.0f; // Упругость (0 = не отскакивает)
        body.createFixture(fixtureDef);

        // 5. Очищаем форму (данные уже скопированы в тело)
        shape.dispose();

        // 6. (Важно!) Устанавливаем пользовательские данные для связи Body и Tile
        body.setUserData(this);
    }

    public Body getBody() {
        return body;
    }
}
