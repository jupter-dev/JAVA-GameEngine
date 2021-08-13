package Entities.Throw;

import Entities.Entity;
import Main.App;
import Processor.Visual.Camera;
import Terrain.World;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.Graphics;

public class Spittle extends Entity{

    private double dx;
    private double dy;
    private double speed = 4;

    private int life = 50, curLife = 0;

    public Spittle(int x, int y, int width, int height, BufferedImage sprite, double dx, double dy){
        super(x, y, width, height, sprite);
        this.dx = dx;
        this.dy = dy;
    }
    
    public void tick(){

        if(World.isCollisionDinamics((int)(x+dx),(int)(y+dy), 3, 3)) {
            x+=dx*speed;
            y+=dy*speed;
        }else {
            App.spittle.remove(this);
            return;
        }
     
        curLife++;
        if(curLife == life){
            App.spittle.remove(this);
            return;
        }
    }
    
    public void render(Graphics g){
        g.setColor(Color.GREEN);
        g.fillOval(this.getX() - Camera.x, this.getY() - Camera.y, 10, 10);
    }
}
