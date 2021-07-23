package Entities.Alives;

import java.awt.image.BufferedImage;
import Entities.Entity;
import Main.App;
import Processor.Camera;
import Terrain.World;
import Terrain.IA.AAstar;
import Terrain.IA.PAstar;

import java.awt.Rectangle;
//import java.awt.Color;
import java.awt.Graphics;


public class Enemy extends Entity{

    private int speed = 1;
    //private int maskx = 11, masky = 12, maskw = 10, maskh = 8;
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
        maskx = 11; masky = 12; maskw = 10; maskh = 8;
            moviment();
            animation();
            //Astar();
    }

    public void render(Graphics g){
        //super.render(g);
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


    public void Astar(){
        if(path == null || path.size() == 0){
            PAstar start = new PAstar((int)(x/32), (int)(y/32));
            PAstar end = new PAstar((int)(App.player.getX()/32), (int)(App.player.getY()/32));
            path = AAstar.findPath(App.world, start, end);
        }
        if(App.random.nextInt(100)< 80){
            findPath(path);
        }
        
    }


    public boolean isCollidingPlayer(){
        Rectangle thisEnemy = new Rectangle(x+maskx, y+masky, maskw,maskh);
        Rectangle player = new Rectangle(App.player.getX()+maskx, App.player.getY()+masky, maskw, maskh);

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
