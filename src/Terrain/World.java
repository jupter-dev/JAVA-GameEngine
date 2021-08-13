package Terrain;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import Entities.Entity;
import Entities.Alives.Enemy;
import Entities.Collectables.Gem;
import Main.App;
import Processor.Visual.Camera;
import Terrain.Type.Floor;
import Terrain.Type.Wall;

import java.awt.Graphics;

public class World {

    public static Tile[] tiles;
    public static int WIDTH, HEIGHT;
    public static final int TILE_SIZE = 32;

    public World(String path){
        try {
            BufferedImage map = ImageIO.read(getClass().getResource(path));

            //pegar tamanho para renderizar
            WIDTH = map.getWidth();
            HEIGHT = map.getHeight();

            //Calcular tamanho do mapa em pixeis e reserva espaço na memória
            int[] pixeis = new int[map.getWidth() * map.getHeight()];
            tiles = new Tile[map.getWidth() * map.getHeight()];


            //Detectar as cores do mapa e atribui o valor em pixel
            map.getRGB(0, 0, map.getWidth(), map.getHeight(), pixeis, 0, map.getWidth());
            
            //Leitura de cima pra baixo com sequencia pra direita
            for(int mx = 0; mx < map.getWidth(); mx++){
                for(int my = 0; my < map.getHeight(); my++){

                    int currentPixel = pixeis[mx+(my*map.getWidth())];

                    int pos = mx + (my * WIDTH), tx = mx*TILE_SIZE, ty = my*TILE_SIZE;

                    tiles[pos] = new Floor(tx, ty, Tile.TILE_GRASS);

                    if(currentPixel == 0xFFFFFFFF){
                        //Parede
                        tiles[pos] = new Wall(tx, ty, Tile.TILE_BRICK);
                    }else if(currentPixel == 0xFF0000FF){
                        //Player
                       App.player.setX(tx);
                       App.player.setY(ty);
                    }else if(currentPixel == 0xFFFF0000){
                        //Inimigo
                        Enemy enemy = new Enemy(tx, ty, TILE_SIZE, TILE_SIZE, Entity.ENEMY);
                        App.enemies.add(enemy);
                        App.entities.add(enemy);
                    }else if(currentPixel == 0xFF00FF00){
                        //GEM
                        Entity gem = new Gem(tx, ty, TILE_SIZE, TILE_SIZE, Entity.GEMS);
                        gem.setMask(11,12,10,8);
                        App.entities.add(gem);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static boolean freeWay(int ox, int oy){
        // Pegar bloco futuro
        int yrt = (oy + TILE_SIZE - 8) / TILE_SIZE;
        int ylt = (oy + TILE_SIZE - 8) / TILE_SIZE;

        int xlt = (ox + TILE_SIZE - 10) / TILE_SIZE;
        int xlb = (ox + TILE_SIZE - 10) / TILE_SIZE;
        
        int xrt = (ox+9) / TILE_SIZE;
        int xrb = (ox+9) / TILE_SIZE;
        
        int yrb = (oy+8) / TILE_SIZE;
        int ylb = (oy+8) / TILE_SIZE;


        return !(tiles[xrb + (yrb*World.WIDTH)] instanceof Wall ||
                 tiles[xlb + (ylb*World.WIDTH)] instanceof Wall ||
                 tiles[xrt + (yrt*World.WIDTH)] instanceof Wall ||
                 tiles[xlt + (ylt*World.WIDTH)] instanceof Wall
        );
    }

    public static boolean freeWayIA(int ox, int oy){
        // Pegar bloco futuro
        int yrt = (oy + TILE_SIZE - 1) / TILE_SIZE;
        int ylt = (oy + TILE_SIZE - 1) / TILE_SIZE;

        int xlt = (ox + TILE_SIZE - 1) / TILE_SIZE;
        int xlb = (ox + TILE_SIZE - 1) / TILE_SIZE;
        
        int xrt = (ox) / TILE_SIZE;
        int xrb = (ox) / TILE_SIZE;
        
        int yrb = (oy) / TILE_SIZE;
        int ylb = (oy) / TILE_SIZE;


        return !(tiles[xrb + (yrb*World.WIDTH)] instanceof Wall ||
                 tiles[xlb + (ylb*World.WIDTH)] instanceof Wall ||
                 tiles[xrt + (yrt*World.WIDTH)] instanceof Wall ||
                 tiles[xlt + (ylt*World.WIDTH)] instanceof Wall
        );
    }

    public static boolean isCollisionDinamics(int xnext, int ynext, int width, int height) {
		int x1 = (xnext + 2) / TILE_SIZE;
		int y1 = (ynext + 7) / TILE_SIZE;
		
		int x2 = (xnext + width - 3) / TILE_SIZE;
		int y2 = ynext / TILE_SIZE;
		
		int x3 = xnext / TILE_SIZE;
		int y3 = (ynext + height - 2) / TILE_SIZE;
		
		int x4 = (xnext + width - 3) / TILE_SIZE;
		int y4 = (ynext + height - 2) / TILE_SIZE;
		
		int out = 20; 
		if(x4 + (y4*World.WIDTH) < out || x2 + (y2*World.WIDTH) < out || x3 + (y3*World.WIDTH) < out || x4 + (y4*World.WIDTH) < out) {
			return false;
		}
		
		return !((tiles[x1 + (y1*World.WIDTH)] instanceof Wall) ||
				(tiles[x2 + (y2*World.WIDTH)] instanceof Wall) ||
				(tiles[x3 + (y3*World.WIDTH)] instanceof Wall) ||
				(tiles[x4 + (y4*World.WIDTH)] instanceof Wall));
	}







    public void render(Graphics g){
        int xs = Camera.x  >> 5;
        int ys = Camera.y >> 5;
        int xf = xs + (App.WIDTH >> 4 );
        int yf = ys + (App.HEIGHT >> 4);

        for(int mx = xs; mx < xf; mx++){
            for(int my = ys; my < yf; my++){
                if(mx < 0 || my < 0 || mx >= WIDTH || my >= HEIGHT)
                    continue;
                Tile tile = tiles[mx + (my*WIDTH)];
                tile.render(g);
            }
        }
    }
}
