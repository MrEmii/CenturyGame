package dev.undervolt.engine.collision;

import org.joml.Vector2f;

public class Collision {

    public Vector2f distance;
    public boolean isIntersecting;

    public Collision(Vector2f distance, boolean isIntersecting) {
        this.distance = distance;
        this.isIntersecting = isIntersecting;
    }

    public Vector2f getDistance() {
        return distance;
    }

    public boolean isIntersecting() {
        return isIntersecting;
    }
}
