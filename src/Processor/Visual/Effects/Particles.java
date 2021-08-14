package Processor.Visual.Effects;

import Entities.Entity;
import Main.App;
import Processor.Visual.Camera;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Particles extends Entity{
    public int lifeTime = 10;
	public int curLife = 0;
	
	public int spd = 2;
	public double dx = 0;
	public double dy = 0;
	public Color cor;

	public Particles(int x, int y, int width, int height, BufferedImage sprite, Color color) {
		super(x, y, width, height, sprite);
		dx = new Random().nextGaussian();
		dy = new Random().nextGaussian();
		this.cor = color;
	}
	
	public static void generateParticles(int amount, int x, int y, Color color) {
		for(int i = 0; i < amount; i++) {
			App.entities.add(new Particles(x,y,1,1,null,color));
		}
	}
	
	public void tick() {
		x+=dx*spd;
		y+=dy*spd;
		curLife++;
		if(lifeTime == curLife) {
			App.entities.remove(this);
		}
	}
	
	public void render(Graphics g) { 
		g.setColor(cor);
		g.fillRect(this.getX() - Camera.x , this.getY() - Camera.y, w, h);
	}
}
