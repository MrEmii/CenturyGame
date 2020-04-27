package dev.undervolt.gui;

import dev.undervolt.engine.graphics.Shader;
import dev.undervolt.engine.io.Window;
import dev.undervolt.engine.objects.Camera;
import dev.undervolt.engine.objects.Model;
import org.joml.Matrix4f;
import org.joml.Vector4f;

public class Gui {

    private Shader shader;
    private Camera camera;

    private Model model;

    public Gui(Window window) {
        shader = new Shader("/shaders/guiVertex.glsl", "/shaders/guiFragment.glsl");
        camera = new Camera(window.getWidth(), window.getHeight());
        float[] vertices = new float[] {
                -1f, 1f, 0, //TOP LEFT     0
                1f, 1f, 0,  //TOP RIGHT    1
                1f, -1f, 0, //BOTTOM RIGHT 2
                -1f, -1f, 0,//BOTTOM LEFT  3
        };

        float[] texture = new float[] {
                0,0,
                1,0,
                1,1,
                0,1,
        };

        int[] indices = new int[] {
                0,1,2,
                2,3,0
        };
        model = new Model(vertices, texture, indices);
    }

    public void resizeCamera(Window window){
        camera.set(window.getWidth(), window.getHeight());
    }

    public void render(){
        Matrix4f mat = new Matrix4f();
        camera.getProjection().scale(87, mat);
        mat.translate(-3, -3, 0);
        shader.bind();

        shader.setUniform("projection", mat);
        shader.setUniform("color", new Vector4f(0,0,0,1));

        model.render();
    }
}
