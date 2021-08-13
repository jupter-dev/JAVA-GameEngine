package Entities.Alives;

import Entities.Entity;
import Entities.Collectables.Gem;
import Entities.Throw.Spittle;

import java.awt.image.BufferedImage;

import Processor.Controllers.Controller;
import Processor.Visual.Camera;

import java.awt.Graphics;
import Main.App;
import Terrain.World;

public class Player extends Entity{

    //Processos
    private Controller input;
    private String side = "R";
    private int camTime = 0;
    public boolean shoot = false;
    public double mx,my;

    //Animação
    private BufferedImage[] ARight;
    private BufferedImage[] ALeft;
    private BufferedImage playerDamage;

    private int frames = 0, maxFrames = 20, index = 0;
    private int camFrames = 0;
    private int damageFrames = 0;

    //Atributos
    private int speed = 2;
    public double life = 5, lifemax = 10;
    public boolean damaged = false;
    
    public Player(int x, int y, int w, int h, BufferedImage sprite, Controller input){
        super(x, y, w, h, sprite);
        this.input = input;

        ARight = new BufferedImage[3];
        ALeft = new BufferedImage[3];
        playerDamage = App.spritesheet.getSprite(0, 64, 32, 32);
        for(int i = 0; i < ALeft.length; i++){
            ARight[i] = App.spritesheet.getSprite(32*i, 0, 32, 32);
            ALeft[i] = App.spritesheet.getSprite(32*i, 0, 32, 32);
        }
    }


    public void tick(){
        depth = 0;
        actions();
        controller();
        camera();
        animation();
        itemsColliding();
    }

    public void render(Graphics g){
        if(!damaged){
            if(side == "R"){
                g.drawImage(ARight[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
            }else{
                g.drawImage(ALeft[index],  this.getX() - Camera.x, this.getY() - Camera.y, null);
            }
        }else{
            g.drawImage(playerDamage, this.getX() - Camera.x, this.getY() - Camera.y, null);
        }
        
    }

    public void actions(){
        if(life <= 0){
            App.gameState = "GAMEOVER";
        }
        if(shoot){
            shoot = false;
            double angle = Math.atan2(my - (this.getY() +8 - Camera.y), mx - (this.getX() +8 - Camera.x));
            double dx = Math.cos(angle);
            double dy = Math.sin(angle);
            int px = 10;
            int py = 10;
            Spittle spittle = new Spittle(this.getX()+px, this.getY()+py, 10, 10, null, dx, dy);
            App.spittle.add(spittle);
        }
    }

    public void itemsColliding(){
        for(int i = 0; i < App.entities.size(); i++){
            Entity e = App.entities.get(i);
            if(e instanceof Gem){
                if(Entity.Colliding(this,e)){
                    life += 5;
                    if(life >= lifemax)
                        life = lifemax;
                    
                    App.entities.remove(i);
                    return;
                }
            }
        }
    }

    public void controller(){
        if(input.up.down && World.freeWay(x, y-speed)){
            y-=speed;
        }else if(input.down.down && World.freeWay(x, y+speed)){
            y+=speed;
        } 
        if(input.right.down && World.freeWay(x+speed, y)){
            x+=speed;
        }else if(input.left.down && World.freeWay(x-speed, y)){
            x-=speed;
        }
    }

    public void animation(){
        frames++;
        if(frames == maxFrames){
            frames = 0;
            index ++;
            if(index >= ALeft.length){
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

    public void camera(){
              
        int xs = (this.getX() - (App.WIDTH/2) + 16);
        int ys = (this.getY() - (App.HEIGHT/2) + 16);

        if(Camera.x > xs){
            camFrames++;
            if(camFrames ==  1){
                camFrames = 0;
                if(Camera.x - xs < 26){
                    Camera.x-=1;
                }else{
                    Camera.x-=speed;
                }
            }
        }else if(Camera.x < xs){
            camFrames++;
            if(camFrames ==  1){
                camFrames = 0;
                if(xs - Camera.x < 26){
                    Camera.x+=1;
                }else{
                    Camera.x+=speed;
                }   
            }
        }
        if(Camera.y > ys){
            camFrames++;
            if(camFrames ==  1){
                camFrames = 0;
                if(Camera.y - ys < 16){
                    Camera.y-=1;
                }else{
                    Camera.y-=speed;
                }
            }
        }else if(Camera.y < ys){
            camFrames++;
            if(camFrames ==  1){
                camFrames = 0;
                if(ys - Camera.y < 16){
                    Camera.y+=1;
                }else{
                    Camera.y+=speed;
                }
            }
        }
        //Pega a posição do personagem o tamanho da tela
        //Subtrair a posição do personagem com a metade da tela, somando a metade da sprite
        if(camTime == 0){
            camTime ++;
            Camera.x = Camera.limit((this.getX() - (App.WIDTH/2) + 16), World.WIDTH*32 - App.WIDTH);
            Camera.y = Camera.limit((this.getY() - (App.HEIGHT/2) + 16), World.HEIGHT*32 - App.HEIGHT);
        }else{
            Camera.y = Camera.limit(Camera.y, World.HEIGHT*32 - App.HEIGHT);
            Camera.x = Camera.limit(Camera.x, World.WIDTH*32 - App.WIDTH);
        }
    }

}
