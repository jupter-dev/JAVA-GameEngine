package Main;
import javax.swing.JFrame;

import Entities.Entity;
import Entities.Alives.Player;
import Processor.Controller;
import Processor.Spritesheet;
import Terrain.World;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.awt.BorderLayout;

public class App extends Canvas implements Runnable{

    private Thread thread;
    public static JFrame window;
    private boolean isRunning = false;
    public static final int WIDTH = 240;
    public static final int HEIGHT = 160;
    public static final int SCALE = 3;

    private BufferedImage image;
    public static Spritesheet spritesheet;
    private Controller controller  = new Controller(this);


    public static World world;
    public static List<Entity> entities;
    public static Player player;

    public App(){
        configInit();
        createWindow();
    }

    public static void main(String[] args) throws Exception {
        App game = new App();
        game.start();
    }

    public void tick(){
        loopTick();
        controller.tick();
    }

    public void render(){
        BufferStrategy bs = this.getBufferStrategy();
        if(bs == null){
            this.createBufferStrategy(3);
            return;
        }

        //Pegar propriedades background
        Graphics g = image.getGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        //Dados visuais do jogo
        loopRender(g);
        
        //Libera todos os recursos acima
        g.dispose();
        
        // Tranformar em Buffer (imagem)
        g = bs.getDrawGraphics();
        g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
        bs.show();
    }

   

    public void configInit(){
        spritesheet = new Spritesheet("/sprites.png");
        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        entities = new ArrayList<Entity>();
        player = new Player(0, 0, 32, 32, spritesheet.getSprite(64, 0, 32, 32), controller);
        entities.add(player);
        world = new World("/map.png");
    }
    

    public void createWindow(){
        setPreferredSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
        setMinimumSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
        setMaximumSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));

        window = new JFrame("Jogo ?");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLayout(new BorderLayout());
        window.add(this, BorderLayout.CENTER);
        window.pack();
        window.setResizable(false);
        window.setVisible(true);
        window.setLocationRelativeTo(null);
    }

    public synchronized void start(){
        thread = new Thread(this);
        isRunning = true;
        thread.start();
    }

    public synchronized void stop(){
        isRunning = false;
    }

    //Listas de objetos criados individualmente
    public void loopTick(){
        for(int i = 0; i < entities.size(); i++){
            Entity entity = entities.get(i);
            entity.tick();
        }
    }

    public void loopRender(Graphics g){
        world.render(g);
        for(int i = 0; i < entities.size(); i++){
            Entity entity = entities.get(i);
            entity.render(g);
        }
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double unprocessed = 0;
        double nsPerTick = 1000000000.0 / 60.0;
        int frames = 0;
        int ticks = 0;
        long lastTime2 = System.currentTimeMillis();

        while(isRunning){
            long now = System.nanoTime();
            unprocessed += (now - lastTime) / nsPerTick;
            lastTime = now;
            boolean newRender = false;
            if(unprocessed >= 1){
                ticks++;
                tick();
                unprocessed -= 1;
                newRender = true;
            }
            try {
                Thread.sleep(2);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(newRender){
                frames ++;
                render();
            }
            if (System.currentTimeMillis() - lastTime2 > 1000){
                lastTime2 += 1000;
                System.out.println(ticks + ": LOGIC | FPS :" + frames );
                frames = 0;
                ticks = 0;
            }
        }
    }
}
