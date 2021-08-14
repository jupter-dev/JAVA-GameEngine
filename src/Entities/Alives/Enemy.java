package Entities.Alives;

import java.awt.image.BufferedImage;
import Entities.Entity;
import Main.App;
import Processor.Sound;
import Processor.Visual.Camera;
import Processor.Visual.Effects.Particles;
import Terrain.World;
import Terrain.IA.AAstar;
import Terrain.IA.PAstar;

import java.awt.Rectangle;
import java.awt.Graphics;
import java.awt.Color;


public class Enemy extends Entity{

    private int speed = 2;
    //private int mx = 11, my = 12, mw = 10, mh = 8;
    private int frames = 0, maxFrames = 20, index = 0;
    private int life = 10;
    private BufferedImage[] Animation;
    private BufferedImage enemyDamage;

    public int damageFrames = 0;
    public boolean damaged = false;

    

    public Enemy(int x, int y, int w, int h, BufferedImage sprite) {
        super(x, y, w, h, sprite);
        Animation = new BufferedImage[2];
        enemyDamage = App.spritesheet.getSprite(0, 64, 32, 32);
        for(int i = 0; i < Animation.length; i++){
            Animation[i] = App.spritesheet.getSprite(32*(4+i), 0, 32, 32);
        }
    }

    public void tick(){
        depth = 1;
        maskx = 11; masky = 12; maskw = 10; maskh = 8;
            moviment();
            animation();
            damageColliding();
            //Astar();
    }

    public void render(Graphics g){
        //super.render(g);
        if(damaged){
            g.drawImage(enemyDamage, x-Camera.x, y-Camera.y, null);
        }else{
            g.drawImage(Animation[index], x-Camera.x, y-Camera.y, null);
        }
        
        //TESTAR A MASCARA DE COLISÃO
        //g.fillRect((x+mx)-Camera.x, (y+my)-Camera.y, mw,mh);
    }

    public void destroyThis(){
        App.entities.remove(this);
        App.enemies.remove(this);
    }


    public void damageColliding(){
        for(int i = 0; i < App.spittle.size(); i++){
           Entity e = App.spittle.get(i);
           if(Entity.Colliding(this, e)){
                life--;
                damaged = true;
                App.spittle.remove(i);
                return;
           }
        }
        if(life <= 0){
            Color colour = new Color(225,0,0);
            Particles.generateParticles(50, this.x, this.y, colour);
            Sound.EnemyDead.play();
            destroyThis();
        }
    }




    public void moviment(){
        if(this.presense(this.getX(), this.getY(), App.player.getX(), App.player.getY()) < 100){
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
                    App.player.damaged = true;
                }
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
        if(damaged){
            damageFrames++;
            if(damageFrames == 8){
                damageFrames = 0;
                damaged = false;
            }
        }
    }
    
}
