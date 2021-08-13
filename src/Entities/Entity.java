package Entities;

import java.awt.image.BufferedImage;
import java.util.Comparator;
import java.util.List;

import Entities.Alives.Enemy;
import Main.App;
import Processor.Visual.Camera;
import Terrain.IA.MAstar;
import Terrain.IA.PAstar;

import java.awt.Rectangle;
import java.awt.Color;
import java.awt.Graphics;

public class Entity {
    //Sprites
    public static BufferedImage GEMS = App.spritesheet.getSprite(32*3, 0, 32, 32);
    public static BufferedImage ENEMY = App.spritesheet.getSprite(32*4, 0, 32, 32);





    //Defaults
    protected int x;
    protected int y;
    protected int w;
    protected int h;

    private BufferedImage sprite;
    public int maskx, masky, maskw, maskh;


    //IA
    protected List<MAstar> path;

    //Profundidade
    public int depth;

    public Entity(int x, int y, int w, int h, BufferedImage sprite){
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.sprite = sprite;

        this.maskx = 0;
        this.masky = 0;
        this.maskw = w;
        this.maskh = h;
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

    public void setMask(int maskx, int masky, int maskw, int maskh){
        this.maskx = maskx;
        this.masky = masky;
        this.maskw = maskw;
        this.maskh = maskh;
    }

    public double presense(int x1, int y1, int x2, int y2){
        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }

    public boolean isColliding(int xnext, int ynext){
        Rectangle thisEnemy = new Rectangle(xnext+maskx, ynext+masky, maskw, maskh);
        for(int i = 0; i < App.enemies.size(); i++){
            Enemy e = App.enemies.get(i);
            if(e == this){
                continue;
            }
            Rectangle touchEnemy = new Rectangle(e.getX()+maskx, e.getY()+masky, maskw, maskh);
            if(thisEnemy.intersects(touchEnemy)){
                return true;
            }
        }

        return false;
    }

    public static boolean Colliding( Entity e1, Entity e2){
        Rectangle e1Mask = new Rectangle(e1.getX() + e1.maskx, e1.getY() + e1.masky, e1.maskw, e1.maskh);
        Rectangle e2Mask = new Rectangle(e2.getX() + e2.maskx, e2.getY() + e2.masky, e2.maskw, e2.maskh);
        
        return e1Mask.intersects(e2Mask);
    }

    public static Comparator<Entity> nodeSorter = new Comparator<Entity>(){
        @Override
        public int compare(Entity n0, Entity n1){
            if(n1.depth < n0.depth)
                return +1;
            if(n1.depth > n0.depth)
                return -1;
            return 0;
        }
    };

    public void tick(){}    

    public void findPath(List<MAstar> path){
        int TILE_SIZE = 32;
        if(path != null){
            if(path.size() > 0){
                PAstar target = path.get(path.size() - 1).tile;

                if(x < target.x * TILE_SIZE){
                    x++;
                }else if(x > target.x * TILE_SIZE){
                    x--;
                }
                if(y < target.y * TILE_SIZE){
                    y++;
                }else if(y > target.y * TILE_SIZE){
                    y--;
                }
                if(x == target.x * TILE_SIZE && y == target.y * TILE_SIZE){
                    path.remove(path.size() - 1);
                }
            }
        }
    }

    public void render(Graphics g){
        g.drawImage(sprite, this.getX() - Camera.x, this.getY() - Camera.y, null);
        g.setColor(Color.BLACK);
        //g.fillRect(this.getX() + maskx - Camera.x, this.getY() + masky - Camera.y, maskw, maskh);
    }

    
}
