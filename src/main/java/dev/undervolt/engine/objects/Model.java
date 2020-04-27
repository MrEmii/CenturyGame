package dev.undervolt.engine.objects;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;

public class Model {

    private int draw_count, vbo_id, tbo_id, ibo_id;

    public Model(float[] vertices, float[] texture_cords, int[] indices) {
        draw_count = indices.length;

        vbo_id = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo_id);
        glBufferData(GL_ARRAY_BUFFER, createBufferF(vertices), GL_STATIC_DRAW);

        tbo_id = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, tbo_id);
        glBufferData(GL_ARRAY_BUFFER, createBufferF(texture_cords), GL_STATIC_DRAW);


        ibo_id = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo_id);

        glBufferData(GL_ELEMENT_ARRAY_BUFFER, createBufferI(indices), GL_STATIC_DRAW);

        glBindBuffer(GL_ARRAY_BUFFER, 0);

    }

    public void render() {
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glBindBuffer(GL_ARRAY_BUFFER, vbo_id);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        //glVertexPointer(3, GL_FLOAT, 0, 0);

        glBindBuffer(GL_ARRAY_BUFFER, tbo_id);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo_id);
        glDrawElements(GL_TRIANGLES, draw_count, GL_UNSIGNED_INT, 0);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
    }

    public FloatBuffer createBufferF(float[] data) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    public IntBuffer createBufferI(int[] data) {
        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    public void update() {

    }

    @Override
    protected void finalize() throws Throwable {
        glDeleteBuffers(vbo_id);
        glDeleteBuffers(tbo_id);
        glDeleteBuffers(ibo_id);
        super.finalize();
    }


}
