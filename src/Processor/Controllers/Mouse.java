package Processor.Controllers;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import Main.App;

public class Mouse implements MouseListener {

    public Mouse(App game) {
		game.addMouseListener(this);
	}

    @Override
    public void mouseClicked(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mousePressed(MouseEvent e) {
        App.player.shoot = true;
        App.player.mx = (e.getX() / 3);
        App.player.my = (e.getY() / 3); 
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }
    
}
