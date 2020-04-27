package dev.undervolt.engine.io;

import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Window {

    private int width, height;
    private String title;

    private long window;

    public Input input;

    public boolean fullscreen, isResized, hasResized = false;

    private GLFWWindowSizeCallback sizeCallback;

    public Window(int width, int height, String title) {
        this.width = width;
        this.height = height;
        this.title = title;
        this.fullscreen = false;
    }

    public void init() {
        if (!glfwInit()) {
            throw new IllegalStateException("GLFW wasn't initializing");
        }

        input = new Input();
        window = glfwCreateWindow(width,
                height,
                title,
                fullscreen ? glfwGetPrimaryMonitor() : 0,
                0);
        if (window == 0) {
            throw new IllegalStateException("Windows wasn't create");
        }

        glfwMakeContextCurrent(window);
        GL.createCapabilities();
        glEnable(GL_DEPTH_TEST);

        this.callbacks();

        if (!fullscreen) {
            GLFWVidMode videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
            glfwSetWindowPos(window, (videoMode.width() - width) / 2, (videoMode.height() - height) / 2);
        }
        glfwShowWindow(window);
        glfwSwapInterval(1);

    }

    public void callbacks() {

        sizeCallback = new GLFWWindowSizeCallback() {
            @Override
            public void invoke(long l, int i, int i1) {
                width = i;
                height = i1;
                hasResized = true;
            }
        };
        glfwSetScrollCallback(window, input.getMouseSrollCallback());
        glfwSetKeyCallback(window, input.getKeyboardCallback());
        glfwSetCursorPosCallback(window, input.getMouseMoveCallback());
        glfwSetMouseButtonCallback(window, input.getMouseButtonsCallback());
        glfwSetWindowSizeCallback(window, sizeCallback);
    }

    public void update() {
        glfwPollEvents();
        GL11.glClearColor(0.7f, 0.8f, 0.9f, 1);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glEnable(GL_TEXTURE_2D);
        glDisable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);



        GL11.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        input.update();
    }

    public void swapBuffers() {
        glfwSwapBuffers(window);
    }

    public void destroy() {
        input.destroy();
        glfwWindowShouldClose(window);
        glfwDestroyWindow(window);
        glfwTerminate();
    }

    public boolean shouldClose() {
        return glfwWindowShouldClose(window);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isFullscreen() {
        return fullscreen;
    }

    public void setFullscreen(boolean fullscreen) {
        this.fullscreen = fullscreen;
    }
}
