package Processor;
import java.awt.Color;
import java.awt.Graphics;

import Main.App;

public class UI {
    public void render(Graphics g){
        g.setColor(Color.BLACK);
        g.fillRect(App.player.getX() - Camera.x + 11, App.player.getY() - Camera.y + 32, 10,2);
        g.setColor(Color.GREEN);
        g.fillRect(App.player.getX() - Camera.x + 11, App.player.getY() - Camera.y + 32, (int)((App.player.life / App.player.lifemax)*10),2);
    }
}
