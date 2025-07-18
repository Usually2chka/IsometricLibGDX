package project.game.Utils;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import project.game.Game.Entitys.Tile;
import project.game.Game.TileMap;

public class InputSystem implements GestureDetector.GestureListener {
    private final OrthographicCamera camera;
    private float initialZoom;
    private final float minZoom = 0.5f;
    private final float maxZoom = 2f;
    private float panSensitivity = 0.4f;

    // Для инерции
    private final Vector2 velocity = new Vector2();
    private final float deceleration = 0.9f; // Коэффициент замедления (0.9 = 10% потерь за кадр)
    private boolean isFlinging = false;

    // Для перемещения при удержании
    private final Vector2 panDelta = new Vector2();
    private boolean isPanning = false;


    public InputSystem(OrthographicCamera camera) {
        this.camera = camera;
        this.initialZoom = camera.zoom;

    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        // Преобразуем координаты касания в координаты мира
        Vector3 touchPos = new Vector3(x, y, 0);
        camera.unproject(touchPos);

        Tile closestTile = null;
        float minDistance = Float.MAX_VALUE;

        for (int row = 15; row >= 0; row--)
            for (int col = 15; col >= 0; col--)
            {
                Tile tile = TileMap.GetMap()[row][col];
                tile.setTouched(false);
                if(tile.isHit(touchPos.x, touchPos.y))
                {
                    float distance = tile.GetDistance(touchPos.x, touchPos.y);
                    if (distance < minDistance) {
                        minDistance = distance;
                        closestTile = tile;
                    }
                }
            }

        if (closestTile != null) {
            closestTile.setTouched(true);
        }
        return true;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        // Инерция после быстрого свайпа
        velocity.set(velocityX * 0.01f, velocityY * 0.01f);
        isFlinging = true;
        return true;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        // Движение камеры при удержании пальца
        panDelta.set(-deltaX * panSensitivity, deltaY * panSensitivity);
        isPanning = true;
        isFlinging = false; // Отключаем инерцию, если палец на экране
        return true;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        // При отпускании пальца сохраняем последнее перемещение для инерции
        if (isPanning) {
            velocity.set(panDelta.x * 0.5f, panDelta.y * 0.5f);
            isFlinging = true;
            isPanning = false;
        }
        return true;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        // Зум
        float zoomFactor = initialDistance / distance;
        camera.zoom = MathUtils.clamp(initialZoom * zoomFactor, minZoom, maxZoom);
        camera.update();
        return true;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

    @Override
    public void pinchStop() {
        // Сохраняем текущий зум для следующего вызова zoom()
        initialZoom = camera.zoom;
    }
    public void updateInertia() {
        // Движение при удержании пальца
        if (isPanning) {
            camera.position.add(panDelta.x, panDelta.y, 0);
            panDelta.set(0, 0); // Сбрасываем дельту
        }

        // Инерция после отпускания
        if (isFlinging && !velocity.isZero(0.1f)) {
            camera.position.add(-velocity.x, velocity.y, 0);
            velocity.scl(deceleration);
            if (velocity.len() < 0.1f) velocity.setZero();
        }

        camera.update();
    }
}
