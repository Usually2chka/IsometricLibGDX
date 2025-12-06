package project.example.Game.Entitys;

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
