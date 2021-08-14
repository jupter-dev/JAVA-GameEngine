package Entities.Alives;

import java.awt.image.BufferedImage;

import Entities.Entity;
import Main.App;

import java.awt.Color;
import java.awt.Graphics;

public class Npc extends Entity {

    public boolean showMessage = false;
    public Npc(int x, int y, int w, int h, BufferedImage sprite) {
        super(x, y, w, h, sprite);
        //TODO Auto-generated constructor stub
    }

    public void tick(){
        int playerX = App.player.getX();
        int playerY = App.player.getY();
        
        if(Math.abs(playerX - this.getX()) < 50 && Math.abs(playerY - this.getY()) < 50){
            showMessage = true;
        }else{
            showMessage = false;
        }
    }

    public void render(Graphics g){
        super.render(g);
        if(showMessage){
            g.setColor(Color.WHITE);
            App.pixelFont = App.pixelFont.deriveFont(18f);
            g.setFont(App.pixelFont);
            g.drawString("AGORA É COM VOCÊ QUADRADO" , (this.getX()) - 50, this.getY() - 30);
            g.drawString("PEGUE ESSE CRISTAL E VÁ ATRÁS DELES" , (this.getX()) - 50, this.getY());
        }
    }

}
