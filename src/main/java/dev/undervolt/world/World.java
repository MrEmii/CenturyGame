package dev.undervolt.world;

import dev.undervolt.engine.collision.AABB;
import dev.undervolt.engine.graphics.Shader;
import dev.undervolt.engine.io.Window;
import dev.undervolt.engine.objects.Camera;
import dev.undervolt.renderer.TileRenderer;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class World {

    private byte[] tiles;
    private int width;
    private int height;
    private int scale;

    private AABB[] bounding_boxes;

    private Matrix4f world;

    public World() {
        width = 75;
        height = 5;
        scale = 16;

        bounding_boxes = new AABB[width*height];

        tiles = new byte[width * height];
        world = new Matrix4f()
                .setTranslation(new Vector3f(0));
        world.scale(scale);

    }

    public void correctCamera(Camera camera, Window window) {
        Vector3f pos = camera.getPosition();

        int w = width * scale * 2;
        int h = height *scale  * 2;

        if (pos.x > -(window.getWidth()) + scale ) {
            pos.x = -(window.getWidth()) + scale;
        }
        if (pos.x < -(w +scale)) {
            pos.x = -(w + scale );
        }

        pos.y = window.getHeight() / 2;
    }

    public void render(TileRenderer render, Window window, Shader shader, Camera camera) {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                render.render(getTile(j, i), j, -i, shader, world, camera);
            }
        }

    }

    public void setTile(Tile tile, int x, int y) {
        tiles[x + y * width] = tile.getId();
        if(tile.isSolid()){
            bounding_boxes[x+y*width] = new AABB(new Vector2f(x*2, -y*2), new Vector2f(1,1));
        }else{
            bounding_boxes[x+y*width] = null;
        }
    }

    public AABB getTileBoundingBox(int x, int y) {
        try {
            return bounding_boxes[x + y * width];
        } catch (NullPointerException | ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    public Tile getTile(int x, int y) {
        try {
            return Tile.tiles[tiles[x + y * width]];
        } catch (NullPointerException | ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    public byte[] getTiles() {
        return tiles;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getScale() {
        return scale;
    }

    public Matrix4f getWorld() {
        return world;
    }
}
