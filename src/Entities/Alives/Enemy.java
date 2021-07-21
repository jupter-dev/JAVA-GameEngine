package Entities.Alives;

import java.awt.image.BufferedImage;

import Entities.Entity;
import Main.App;
import Processor.Camera;
import Terrain.World;
import java.awt.Rectangle;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Enemy extends Entity{

    private int speed = 1;
    private int mx = 11, my = 12, mw = 10, mh = 8;
    private int frames = 0, maxFrames = 20, index = 0;
    private BufferedImage[] Animation;

    public Enemy(int x, int y, int w, int h, BufferedImage sprite) {
        super(x, y, w, h, sprite);
        Animation = new BufferedImage[2];
        for(int i = 0; i < Animation.length; i++){
            Animation[i] = App.spritesheet.getSprite(32*(4+i), 0, 32, 32);
        }
    }

    public void tick(){
            moviment();
            animation();
    }

    public void render(Graphics g){
        super.render(g);
        g.setColor(Color.black);
        g.drawImage(Animation[index], x-Camera.x, y-Camera.y, null);
        //g.fillRect((x+mx)-Camera.x, (y+my)-Camera.y, mw,mh);
    }


    public void moviment(){
        if(isCollidingPlayer() == false){
            if((int)x > App.player.getX() && World.freeWay(x-speed, y) && !isColliding(x-speed, y)){
                x-=speed;
            }else if((int)x < App.player.getX() && World.freeWay(x+speed, y) && !isColliding(x+speed, y)){
                x+= speed;
            }
            if((int)y < App.player.getY() && World.freeWay(x, y+speed) && !isColliding(x, y+speed)){
                y+=speed;
            }
            else if((int)y > App.player.getY() && World.freeWay(x, y-speed) && !isColliding(x, y-speed)){
                y-=speed;
            } 
        }else{
            if(App.random.nextInt(100) < 10){
                App.player.life-=App.random.nextInt(3);
                if(App.player.life <= 0){
                    //GameOver
                }
                System.out.println("Vida "+ App.player.life);
            }
        }
    }

    public boolean isColliding(int xnext, int ynext){
        Rectangle thisEnemy = new Rectangle(xnext+mx, ynext+my, mw, mh);
        for(int i = 0; i < App.enemies.size(); i++){
            Enemy e = App.enemies.get(i);
            if(e == this){
                continue;
            }
            Rectangle touchEnemy = new Rectangle(e.getX()+mx, e.getY()+my, mw, mh);
            if(thisEnemy.intersects(touchEnemy)){
                return true;
            }
        }

        return false;
    }

    public boolean isCollidingPlayer(){
        Rectangle thisEnemy = new Rectangle(x+mx, y+my, mw,mh);
        Rectangle player = new Rectangle(App.player.getX()+mx, App.player.getY()+my, mw, mh);

        return thisEnemy.intersects(player);
    }

    public void animation(){
        frames++;
        if(frames == maxFrames){
            frames = 0;
            index ++;
            if(index >= Animation.length){
                index = 0;
            }
        }
    }
    
}
