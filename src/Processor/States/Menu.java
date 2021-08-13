package Processor.States;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.io.File;

import Processor.Controllers.Controller;
import Processor.Imports.SaveLoad;
import Main.App;

public class Menu {

    private Controller input;
    private String[] options =   {"Jogar", "Carregar", "Opções", "Sair"};
    private int currentSelection = 0;
    private int maxOption = options.length - 1;

    public Menu(Controller controller){
        input = controller;
    }


    public void tick(){
       if(input.up.down){
            currentSelection--;
            input.up.down = false;
            if(currentSelection < 0){
                currentSelection = maxOption;
            }
       }
       if(input.down.down){
           currentSelection++;
           input.down.down = false;
           if(currentSelection > maxOption){
                currentSelection = 0;
            }
       }

       if(input.enter.down && currentSelection == 0){
           App.gameState = "NORMAL";
       }
       if(input.enter.down && currentSelection == 1){
          input.enter.down = false;
          File file = new File("save.sg");
          if(file.exists()){
            SaveLoad.StartLoad(20);
          }
        }
    }

    public void render(Graphics g){
        g.setColor(Color.black);
        g.fillRect(0,0, App.WIDTH*App.SCALE, App.HEIGHT*App.SCALE);

        g.setColor(Color.CYAN);
        g.setFont(new Font("Arial", Font.BOLD, 36));
        g.drawString("ATTACK ON QUADRADOS", ((App.WIDTH*App.SCALE) / 2) - 230, 200);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString("Em desenvolvimento", ((App.WIDTH*App.SCALE) / 2) - 230, 160);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 22));
        g.drawString(options[0], ((App.WIDTH*App.SCALE) / 2) - 37, 300);
        g.drawString(options[1], ((App.WIDTH*App.SCALE) / 2) - 50, 350);
        g.drawString(options[2], ((App.WIDTH*App.SCALE) / 2) - 45, 400);
        g.drawString(options[3], ((App.WIDTH*App.SCALE) / 2) - 27, 450);

        g.drawString(">", ((App.WIDTH*App.SCALE) / 2) - 100, 300 + (50*currentSelection));
        g.drawString("<", ((App.WIDTH*App.SCALE) / 2) + 65, 300 + (50*currentSelection));
    }
}
