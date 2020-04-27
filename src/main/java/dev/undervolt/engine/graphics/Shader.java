package dev.undervolt.engine.graphics;

import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL20.*;

public class Shader {
    private int program, vertex_shader, fragment_shader;

    public Shader(String vertex, String fragment) {

        program = glCreateProgram();

        vertex_shader = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertex_shader, readFile(vertex));
        glCompileShader(vertex_shader);
        if (glGetShaderi(vertex_shader, GL_COMPILE_STATUS) == GL_FALSE) {
            System.err.println("Vertex Shader: " + glGetShaderInfoLog(vertex_shader));
            return;
        }

        fragment_shader = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragment_shader, readFile(fragment));
        glCompileShader(fragment_shader);
        if (glGetShaderi(fragment_shader, GL_COMPILE_STATUS) == GL_FALSE) {
            System.err.println("Fragment Shader: " + glGetShaderInfoLog(fragment_shader));
            return;
        }

        glAttachShader(program, vertex_shader);
        glAttachShader(program, fragment_shader);

        glBindAttribLocation(program, 0, "vertices");
        glBindAttribLocation(program, 1, "textures");

        glLinkProgram(program);
        if (glGetProgrami(program, GL_LINK_STATUS) == GL_FALSE) {
            System.err.println("Program Linking: " + glGetProgramInfoLog(program));
            return;
        }
        glValidateProgram(program);
        if (glGetProgrami(program, GL_VALIDATE_STATUS) == GL_FALSE) {
            System.err.println("Program Linking: " + glGetProgramInfoLog(program));
            return;
        }
    }

    public void bind() {
        glUseProgram(program);
    }

    public int getUniformLocation(String name) {
        return glGetUniformLocation(program, name);
    }
    public void setUniform(String name, int value){
        int location = getUniformLocation(name);
        glUniform1i(location, value);
    }
    public void setUniform(String name, float value){
        int location = getUniformLocation(name);
        glUniform1f(location, value);
    }
    public void setUniform(String name, boolean value){
        int location = getUniformLocation(name);
        glUniform1i(getUniformLocation(name), value ? 1 : 0);
    }
    public void setUniform(String name, Vector4f value) {
        int location = getUniformLocation(name);
        if(location != -1)
            glUniform4f(location, value.x, value.y, value.z, value.w);
    }
    public void setUniform(String name, Matrix4f value){
        int location = getUniformLocation(name);
        FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
        value.get(buffer);
        glUniformMatrix4fv(location, false, buffer);
    }
    public void unbind() {
        glUseProgram(0);
    }

    private String readFile(String path){
        StringBuilder result = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Shader.class.getResourceAsStream(path)))) {
            String line = "";
            while ((line = reader.readLine()) != null) {
                result.append(line).append("\n");
            }
        } catch (IOException e) {
            System.err.println("Could not find the file at" + path + ". " + e.getMessage());
        }
        return result.toString();
    }

    @Override
    protected void finalize() throws Throwable {
        glDetachShader(program, vertex_shader);
        glDetachShader(program, fragment_shader);
        glDeleteShader(vertex_shader);
        glDeleteShader(fragment_shader);
        glDeleteProgram(program);
        super.finalize();
    }
}
