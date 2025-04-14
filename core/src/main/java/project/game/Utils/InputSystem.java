package project.game.Utils;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class InputSystem implements GestureDetector.GestureListener {
    private final OrthographicCamera camera;
    private float initialZoom;
    private final float minZoom = 0.5f;
    private final float maxZoom = 2f;

    public InputSystem(OrthographicCamera camera) {
        this.camera = camera;
        this.initialZoom = camera.zoom;
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
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
        // Движение камеры при свайпе
        float speed = 0.01f; // Настройте под чувствительность
        camera.position.add(-velocityX * speed, velocityY * speed, 0);
        return true;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        // Обработка пинча (зум)
        float zoomFactor = initialDistance / distance;
        camera.zoom = initialZoom * zoomFactor;
        camera.zoom = MathUtils.clamp(camera.zoom, minZoom, maxZoom);
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
}
