package dev.undervolt.engine.io;

import org.lwjgl.glfw.*;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_LAST;

public class Input {

    private static boolean[] keys = new boolean[GLFW_KEY_LAST];
    private static boolean[] buttons = new boolean[GLFW.GLFW_MOUSE_BUTTON_LAST];
    private static double mouseX, mouseY;
    private static double scrollX, scrollY;

    private GLFWKeyCallback keyboard;
    private GLFWMouseButtonCallback mouseButtons;
    private GLFWCursorPosCallback mouseMove;
    private GLFWScrollCallback mouseScroll;

    public Input() {

        keyboard = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                keys[key] = (action != GLFW.GLFW_RELEASE);

            }
        };

        mouseMove = new GLFWCursorPosCallback() {
            @Override
            public void invoke(long l, double v, double v1) {
                mouseX = v;
                mouseY = v1;
            }
        };

        mouseButtons = new GLFWMouseButtonCallback() {
            @Override
            public void invoke(long l, int i, int i1, int i2) {
                buttons[i] = (i1 != GLFW.GLFW_RELEASE);
            }
        };

        mouseScroll = new GLFWScrollCallback() {
            @Override
            public void invoke(long l, double v, double v1) {
                scrollX += v;
                scrollY += v1;
            }
        };
    }

    public static boolean isKeyDown(int key) {
        return keys[key];
    }

    public static boolean isButtonPress(int button) {
        return buttons[button];
    }


    public boolean isKeyPressed(int key) {
        return (isKeyDown(key) && !keys[key]);
    }

    public boolean isKeyReleased(int key) {
        return (!isKeyDown(key) && keys[key]);
    }

    public void update() {
        for(int i = 0; i < GLFW_KEY_LAST; i++)
            keys[i] = isKeyDown(i);
    }

    public void destroy() {
        keyboard.free();
        mouseMove.free();
        mouseButtons.free();
        mouseScroll.free();
    }

    public static double getMouseX() {
        return mouseX;
    }

    public static double getMouseY() {
        return mouseY;
    }

    public GLFWKeyCallback getKeyboardCallback() {
        return keyboard;
    }

    public GLFWMouseButtonCallback getMouseButtonsCallback() {
        return mouseButtons;
    }

    public GLFWCursorPosCallback getMouseMoveCallback() {
        return mouseMove;
    }

    public GLFWScrollCallback getMouseSrollCallback() {
        return mouseScroll;
    }

    public static double getScrollX() {
        return scrollX;
    }

    public static double getScrollY() {
        return scrollY;
    }


}
