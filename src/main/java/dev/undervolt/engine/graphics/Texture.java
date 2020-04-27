package dev.undervolt.engine.graphics;


import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBImage;

public class Texture {
    private int textureObject;
    private int width;
    private int height;

    public Texture(String filename) {
        File file = new File("resources/textures/" + filename);

        IntBuffer widthBuffer = BufferUtils.createIntBuffer(1);
        IntBuffer heightBuffer = BufferUtils.createIntBuffer(1);
        IntBuffer compBuffer = BufferUtils.createIntBuffer(1);

        ByteBuffer data = STBImage.stbi_load(file.getAbsolutePath(), widthBuffer, heightBuffer, compBuffer, STBImage.STBI_rgb_alpha);

        textureObject = glGenTextures();
        this.width = widthBuffer.get();
        this.height = heightBuffer.get();

        glBindTexture(GL_TEXTURE_2D, textureObject);

        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, this.width, this.height, 0, GL_RGBA, GL_UNSIGNED_BYTE, data);

        STBImage.stbi_image_free(data);


    }

    @Override
    protected void finalize() throws Throwable {
        glDeleteTextures(textureObject);
        super.finalize();
    }

    public void bind(int sampler) {
        if (sampler >= 0 && sampler <= 31) {
            glActiveTexture(GL_TEXTURE0 + sampler);
            glBindTexture(GL_TEXTURE_2D, textureObject);
        }
    }
}