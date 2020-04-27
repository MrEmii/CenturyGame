package dev.undervolt.entity;

import dev.undervolt.engine.collision.AABB;
import dev.undervolt.engine.graphics.Animation;
import dev.undervolt.engine.graphics.Shader;
import dev.undervolt.engine.graphics.Texture;
import dev.undervolt.engine.io.Window;
import dev.undervolt.engine.objects.Camera;
import dev.undervolt.engine.objects.Model;
import dev.undervolt.engine.objects.Transform;
import dev.undervolt.world.World;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public interface Entity {
    public Transform getTransform();

    public Model getModel();

    public Texture getTexture();

    public AABB getBox();

    public Animation[] getAnimations();

    public void init();

    public void destroy();

    public void render(Shader shader, Camera camera, World world);

    public void update(float delta, Window window, Camera camera, World world);
}