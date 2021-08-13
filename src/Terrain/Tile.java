package Terrain;

import java.awt.image.BufferedImage;
import java.awt.Graphics;

import Main.App;
import Processor.Visual.Camera;

public class Tile {
    private BufferedImage tile;
    private int x,y;

    public static BufferedImage TILE_GRASS = App.spritesheet.getSprite(0, 32, 32, 32);
    public static BufferedImage TILE_BRICK = App.spritesheet.getSprite(32, 32, 32, 32);

    public Tile(int x, int y, BufferedImage tile){
        this.x = x;
        this.y = y;
        this.tile = tile;
    }

    public void render(Graphics g){
        g.drawImage(tile, x - Camera.x, y-Camera.y, null);
    }
}
