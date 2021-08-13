package Processor.States;

import Processor.Controllers.Controller;
import Processor.Imports.SaveLoad;

import java.awt.Color;
import java.awt.Graphics;

import Main.App;

public class Pause {
    private Controller input;
    private boolean saveGame = true;

    public Pause(Controller input){
        this.input = input;
    }

    public void tick(){
        if(input.esc.down){
            input.esc.down = false;
            App.gameState = "NORMAL";
            saveGame = true;
        }
        if(saveGame){
            saveGame = false;
            String[] info1 = {"level"};
            int[] info2 = {App.CUR_LEVEL};
            SaveLoad.SaveGame(info1, info2, 20);
        }
    }

    public void render(Graphics g){
        g.setColor(new Color(0,0,0,175));
        g.fillRect(0,0, App.WIDTH*App.SCALE, App.HEIGHT*App.SCALE);

        g.setColor(Color.white);
       // g.setFont(new Font("Arial", Font.BOLD, 24));
        App.pixelFont = App.pixelFont.deriveFont(50f);
        g.setFont(App.pixelFont);
        g.drawString("PAUSADO", ((App.WIDTH*App.SCALE) / 2) - 50, 200);

        App.pixelFont = App.pixelFont.deriveFont(30f);
        g.setFont(App.pixelFont);
        g.drawString("JOGO SALVO", (App.WIDTH * App.SCALE) - 150, (App.HEIGHT * App.SCALE) - 10);
    }
}
