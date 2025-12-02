package project.example.Game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class IsometricRender extends Stage {
    private ShapeRenderer shapeRenderer = new ShapeRenderer();

    public IsometricRender()
    {
    }
    public void Render(SpriteBatch batch)
    {

        for (int row = 15; row >= 0; row--)
            for (int col = 15; col >= 0; col--)
            {
                if (TileMap.GetMap()[row][col].IsTouched()) {
                    batch.setColor(0.375f, 0.42f, 0.54f, 1);
                } else {
                    batch.setColor(Color.WHITE);
                }

                batch.draw(TileMap.GetMap()[row][col].GetTextureRegion(),
                           TileMap.GetMap()[row][col].GetRectangle().x,
                           TileMap.GetMap()[row][col].GetRectangle().y,
                           TileMap.GetMap()[row][col].GetRectangle().width,
                           TileMap.GetMap()[row][col].GetRectangle().height);
                batch.setColor(Color.WHITE); // сброс цвета
            }
    }

    public void Debuging()
    {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.BLUE);
        for (int row = 15; row >= 0; row--)
            for (int col = 15; col >= 0; col--)
                shapeRenderer.rect(TileMap.GetMap()[row][col].GetRectangle().x,
                    TileMap.GetMap()[row][col].GetRectangle().y,
                    TileMap.GetMap()[row][col].GetRectangle().getWidth(),
                    TileMap.GetMap()[row][col].GetRectangle().getHeight());
        shapeRenderer.end();
    }
}
