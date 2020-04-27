package dev.undervolt.entity;

import dev.undervolt.engine.collision.AABB;
import dev.undervolt.engine.collision.Collision;
import dev.undervolt.engine.graphics.Animation;
import dev.undervolt.engine.graphics.Shader;
import dev.undervolt.engine.graphics.Texture;
import dev.undervolt.engine.io.Input;
import dev.undervolt.engine.io.Window;
import dev.undervolt.engine.objects.Camera;
import dev.undervolt.engine.objects.Model;
import dev.undervolt.engine.objects.Transform;
import dev.undervolt.world.World;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;


public abstract class AbstractEntity implements Entity {

    public Model model;
    public Texture texture;
    public Animation[] animations;
    public AABB box;
    public Transform transform;
    int currentAnimation;


    @Override
    public AABB getBox() {
        return box;
    }

    @Override
    public Animation[] getAnimations() {
        return animations;
    }

    @Override
    public Transform getTransform() {
        return transform;
    }

    @Override
    public Model getModel() {
        return model;
    }

    @Override
    public Texture getTexture() {
        return texture;
    }


    public void init() {
        float[] vertices = new float[]{
                -1f, 1f, 0, //TOP LEFT     0
                1f, 1f, 0,  //TOP RIGHT    1
                1f, -1f, 0, //BOTTOM RIGHT 2
                -1f, -1f, 0,//BOTTOM LEFT  3
        };

        float[] textures = new float[]{
                0, 0,
                1, 0,
                1, 1,
                0, 1,
        };

        int[] indices = new int[]{
                0, 1, 2,
                2, 3, 0
        };

        model = new Model(vertices, textures, indices);
        animations = new Animation[156];
    }

    @Override
    protected void finalize() throws Throwable {
        destroy();
        super.finalize();
    }

    public abstract void destroy();

    public void render(Shader shader, Camera camera, World world) {
        shader.bind();
        shader.setUniform("sampler", 0);
        shader.setUniform("projection", transform.getProjection(camera.getProjection()));
        animations[currentAnimation].bind();
        model.render();
    }

    public void move(Vector2f direction) {
        transform.pos.add(new Vector3f(direction, 0));
        box.getCenter().set(transform.pos.x, transform.pos.y);
    }


    public void collision(World world) {
        box.getCenter().set(transform.pos.x, transform.pos.y);

        AABB[] boxes = new AABB[25];

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                boxes[i + j * 5] = world.getTileBoundingBox(
                        (int) ((transform.pos.x / 2) + 0.5f) - (5 / 2) + i,
                        (int) ((-transform.pos.y / 2) + 0.5f) - (5 / 2) + j
                );
            }
        }

        AABB bounding = null;
        for (int i = 0; i < boxes.length; i++) {
            if (boxes[i] != null) {
                if (bounding == null) bounding = boxes[i];
                Vector2f length1 = bounding.getCenter().sub(transform.pos.x, transform.pos.y, new Vector2f());
                Vector2f length2 = boxes[i].getCenter().sub(transform.pos.x, transform.pos.y, new Vector2f());

                if (length1.lengthSquared() > length2.lengthSquared()) {
                    bounding = boxes[i];
                }
            }
        }
        if (bounding != null) {
            Collision data = box.getCollision(bounding);
            if (data.isIntersecting) {
                box.correctPosition(bounding, data);
                transform.pos.set(box.getCenter(), 0);
            }
        }
    }

    public void setAnimation(int index, Animation animation) {
        this.animations[index] = animation;
    }

    public void  useAnimation(int index){
        this.currentAnimation = index;
    }
}
