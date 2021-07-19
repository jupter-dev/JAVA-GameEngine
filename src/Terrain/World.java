package Terrain;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import Entities.Entity;
import Entities.Alives.Enemy;
import Entities.Collectables.Gem;
import Main.App;
import Terrain.Type.Floor;
import Terrain.Type.Wall;
import Processor.Camera;

import java.awt.Graphics;

public class World {

    private Tile[] tiles;
    public static int WIDTH, HEIGHT;

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

                    int pos = mx + (my * WIDTH), tx = mx*32, ty = my*32;

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
                        App.entities.add(new Enemy(tx, ty, 32, 32, Entity.ENEMY));
                    }else if(currentPixel == 0xFF00FF00){
                        //GEM
                        App.entities.add(new Gem(tx, ty, 32, 32, Entity.GEMS));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void render(Graphics g){
        int xs = Camera.x  >> 5;
        int ys = Camera.y >> 5;
        int xf = xs + (App.WIDTH >> 4);
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
