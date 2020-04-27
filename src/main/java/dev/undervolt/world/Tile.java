package dev.undervolt.world;

public class Tile {

    public static Tile[] tiles = new Tile[16];

    public static final Tile dirt = new Tile((byte) 0, "dirt");
    public static final Tile grass = new Tile((byte) 1, "grass").setSolid();
    public static final Tile barrier = new Tile((byte) 2, "barrier");
    private  byte id;
    private String texture;
    private boolean isSolid;

    public Tile(byte id, String texture) {
        this.id = id;
        this.texture = texture;
        this.isSolid = false;
        if(tiles[id] != null)
            throw  new IllegalStateException("Tile at [" + id + "] is already being used!");
        tiles[id] = this;
    }

    public byte getId() {
        return id;
    }

    public Tile setSolid(){
        this.isSolid = true;
        return this;
    }

    public boolean isSolid() {
        return isSolid;
    }

    public String getTexture() {
        return texture;
    }
}
