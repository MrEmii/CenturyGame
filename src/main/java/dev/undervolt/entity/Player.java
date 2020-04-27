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

public class Player extends AbstractEntity {

    public static final int IDLE = 0;
    public static final int WALKL = 1;
    public static final int WALKR = 2;
    public static final int JUMP = 3;

    public void init() {
        super.init();
        //this.animation = new Animation(4, 5, "idle/idle-");
        setAnimation(IDLE, new Animation(4, 2, "idle/idle-"));
        setAnimation(WALKL, new Animation(4, 5, "run/left/run-"));
        setAnimation(WALKR, new Animation(4, 5, "run/right/run-"));
        setAnimation(JUMP, new Animation(2, 5, "jump/jump-"));
        useAnimation(0);
        transform = new Transform();
        transform.scale = new Vector3f(16, 16, 1);

        box = new AABB(new Vector2f(transform.pos.x, transform.pos.y), new Vector2f(1, 1));
    }

    public void destroy() {
        System.out.println("pene");
    }



    public float yspeed = 1;

    public boolean jumpPressed, jumpWasPressed;

    @Override
    public void update(float delta, Window window, Camera camera, World world) {

        transform.pos.y += yspeed;
        Vector2f movement = new Vector2f();
        yspeed -= 0.1;
        System.out.println(transform.pos.y);
        if (transform.pos.x > -1 && transform.pos.y <= 0f) {
            yspeed = 0;
            transform.pos.y = 0f;

            if (jumpPressed && !jumpWasPressed) {
                movement.add(new Vector2f(0, 10 * delta));

                yspeed = 1;
                if (Input.isKeyDown(GLFW.GLFW_KEY_A)) {
                    movement.add(new Vector2f(-20 * delta, 0));
                }
                if (Input.isKeyDown(GLFW.GLFW_KEY_D)) {
                    movement.add(new Vector2f(20 * delta, 0));

                }
            }
        }
        jumpWasPressed = jumpPressed;
        jumpPressed = Input.isKeyDown(GLFW.GLFW_KEY_SPACE);


        if (Input.isKeyDown(GLFW.GLFW_KEY_A))
            movement.add(-10 * delta, 0);
        if (Input.isKeyDown(GLFW.GLFW_KEY_D))
            movement.add(10 * delta, 0);

        collision(world);

        move(movement);

        if(movement.x != 0 || movement.y != 0){
            if(movement.x > 0){
                useAnimation(WALKR);
            }else{
                useAnimation(WALKL);
            }
        }
        else if(jumpPressed && !jumpWasPressed){
            useAnimation(JUMP);
        }
        else{
            useAnimation(IDLE);
        }

        camera.getPosition().lerp(transform.pos.mul(-(world.getScale() + 8), new Vector3f()), 0.1f);
        // camera.setPosition(transform.pos.mul(-(world.getScale() + 8), new Vector3f()));
    }




}
