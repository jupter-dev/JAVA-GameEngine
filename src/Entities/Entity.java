package Entities;

import java.awt.image.BufferedImage;

import Main.App;
import Processor.Camera;

import java.awt.Graphics;

public class Entity {
    //Sprites
    public static BufferedImage GEMS = App.spritesheet.getSprite(32*3, 0, 32, 32);
    public static BufferedImage ENEMY = App.spritesheet.getSprite(32*4, 0, 32, 32);





    //Defaults
    protected int x;
    protected int y;
    private int w;
    private int h;

    private BufferedImage sprite;

    public Entity(int x, int y, int w, int h, BufferedImage sprite){
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.sprite = sprite;
    }

    public int getX(){
        return this.x;
    }

    public int getY(){
        return this.y;
    }

    public int getWidth(){
        return this.w;
    }

    public int getHeight(){
        return this.h;
    }

    public void setX(int newX){
        this.x = newX;
    }

    public void setY(int newY){
        this.y = newY;
    }

    public void tick(){

    }

    public void render(Graphics g){
        g.drawImage(sprite, this.getX() - Camera.x, this.getY() - Camera.y, null);
    }
}
