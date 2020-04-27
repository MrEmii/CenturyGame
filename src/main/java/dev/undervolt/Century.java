package dev.undervolt;

import dev.undervolt.engine.graphics.Shader;
import dev.undervolt.engine.graphics.Texture;
import dev.undervolt.engine.io.Input;
import dev.undervolt.engine.io.Timer;
import dev.undervolt.engine.io.Window;
import dev.undervolt.engine.objects.Camera;
import dev.undervolt.engine.objects.Model;
import dev.undervolt.entity.Player;
import dev.undervolt.gui.Gui;
import dev.undervolt.renderer.TileRenderer;
import dev.undervolt.world.Tile;
import dev.undervolt.world.World;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.glViewport;

public class Century implements Runnable {

    private Thread game;
    private Window window;

    private Player player;

    private Timer timer;

    private Matrix4f scale = new Matrix4f()
            .translate(0, 0, 0).scale(16);
    private Matrix4f target = new Matrix4f();

    private World world;
    private Shader shader;
    private Gui gui;
    private Camera camera;

    public void start() {
        game = new Thread(this, "game");
        game.start();
    }


    TileRenderer tiles;


    public void init() {
        System.out.println("Initializing Game");
        window = new Window(1280, 720, "Century Game");
        window.init();
        timer = new Timer();
        shader = new Shader("/shaders/mainVertex.glsl", "/shaders/mainFragment.glsl");
        this.camera = new Camera(window.getWidth(), window.getHeight());
        this.tiles = new TileRenderer();
        this.world = new World();
        player = new Player();
        player.init();
        camera.setPosition(new Vector3f(-100, 0, 0));
        gui = new Gui(window);
    }


    private void update() {
        window.update();

        timer.update();
        player.update((float) timer.frame_cap, window, camera, world);
        if (window.hasResized) {
            camera.set(window.getWidth(), window.getHeight());
            gui.resizeCamera(window);
            glViewport(0, 0, window.getWidth(), window.getHeight());
        }
        world.correctCamera(camera, window);
        target = scale;
    }

    public void close() {
        player.destroy();
        window.destroy();
    }


    private void render() {
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0, 1280, 0, 720, 1, -1);


        world.render(tiles, window, shader, camera);
        for (int i = 0; i < world.getWidth(); i++) {
            world.setTile(Tile.barrier, i, 0);
        }
        for (int i = 0; i < world.getWidth(); i++) {
            world.setTile(Tile.grass, i, 1);
        }
        world.setTile(Tile.grass, 4, 0);
        world.setTile(Tile.grass, 5, 0);
        world.setTile(Tile.grass, 6, 0);
        player.render(shader, camera, world);

        gui.render();
        window.swapBuffers();

    }

    public static void main(String[] args) {
        new Century().start();
    }


    @Override
    public void run() {
        this.init();
        while (!window.shouldClose()) {
            update();
            render();
        }
        this.close();

    }
}
