package Main;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

import Entities.Entity;
import Entities.Alives.Enemy;
import Entities.Alives.Npc;
import Entities.Throw.Spittle;
import Entities.Alives.Player;
import Processor.Controllers.Controller;
import Processor.Controllers.Mouse;
import Processor.Imports.Spritesheet;
import Processor.Visual.UI;
import Processor.Visual.States.Menu;
import Processor.Visual.States.Pause;
import Terrain.World;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;


public class App extends Canvas implements Runnable{
	private static final long serialVersionUID = 1L;
	public static Random random;
    private Thread thread;
    public static JFrame window;
    private boolean isRunning = false;
    static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    public static final int SCALE = 3;
    public static final int WIDTH  = (int)Math.ceil((int)screenSize.getWidth() / 3) + 1 ;
    public static final int HEIGHT = (int)Math.ceil((int)screenSize.getHeight() / 3) + 1;


    private BufferedImage image;
    public static Spritesheet spritesheet;
    private Controller controller  = new Controller(this);
    private Mouse mouse  = new Mouse(this);


    public static World world;
    public static List<Entity> entities;
    public static List<Enemy> enemies;
    public static List<Spittle> spittle;
    public static Player player;
    public Npc npc;
    public UI ui;

    public InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream("pixel.ttf");
    public static Font pixelFont;
    
    public Menu menu;
    public Pause pause;


    //STATUS GAME
    public static boolean resetar = false;
    public static int CUR_LEVEL = 1, MAX_LEVEL = 2;
    private String newWorld = "";
    public static String gameState = "MENU";
    private boolean showGameOver = true;
    private int framesGameOver = 0;


    public App(){
        fontInit();
        configInit();
        createWindow();
    }

    /* Só pra remover o Warning, depois arrumar */
    public Mouse getMouse() {
        return mouse;
    }

    public void setMouse(Mouse mouse) {
        this.mouse = mouse;
    }
    /* ====================================== */
    
    public static void main(String[] args) throws Exception {
        App game = new App();
        game.start();
    }

    public void tick(){
        restart();
        if(gameState == "NORMAL"){
            loopTick();
            controller.tick();
            if(enemies.size() == 0){
                CUR_LEVEL++;
                if(CUR_LEVEL > MAX_LEVEL){
                    CUR_LEVEL = 1;
                }
                 newWorld = "/map"+CUR_LEVEL+".png";
                 resetar = true;
            }
            if(controller.esc.down){
                controller.esc.down = false;
                gameState = "PAUSE";
            }
        }else if (gameState == "GAMEOVER"){
            framesGameOver++;
            if(framesGameOver == 40){
                framesGameOver = 0;
                if(showGameOver)
                    showGameOver = false;
                        else
                            showGameOver = true;
            }

            if(controller.enter.down){
                resetar = true;
            }
        }else if(gameState == "MENU"){
            menu.tick();
        }else if(gameState == "PAUSE"){
            pause.tick();
        }
        

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
        // Acima tudo pixel

        //TESTANDO
        if(gameState == "GAMEOVER"){
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(new Color(0,0,0,175));
            g2.fillRect(0, 0, WIDTH*SCALE, HEIGHT*SCALE);
            g.setFont(new Font("arial", Font.BOLD, 40));
            g.setColor(Color.white);
            g.drawString("Game Over", ((WIDTH*SCALE) / 2) - 105 , (HEIGHT*SCALE) / 2);
            g.setFont(new Font("arial", Font.BOLD, 30));
            if(showGameOver)
                g.drawString(">>Press ENTER to restart<<", ((WIDTH*SCALE) / 2) - 200 , ((HEIGHT*SCALE) / 2)+80);
        }else if(gameState == "MENU"){
            menu.render(g);
        }else if(gameState == "PAUSE"){
            pause.render(g);
        }
        bs.show();
    }

    public void fontInit(){
        try {
            pixelFont = Font.createFont(Font.TRUETYPE_FONT, stream);
        } catch (FontFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void configInit(){
        random = new Random();
        ui = new UI();
        spritesheet = new Spritesheet("/sprites.png");
        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        entities = new ArrayList<Entity>();
        enemies = new ArrayList<Enemy>();
        spittle = new ArrayList<Spittle>();
        player = new Player(0, 0, 32, 32, spritesheet.getSprite(64, 0, 32, 32), controller);
        entities.add(player);
        world = new World("/map1.png");
        menu = new Menu(controller);
        pause = new Pause(controller);
        npc = new Npc(5*32, 2*32, 32,32, spritesheet.getSprite(32*2, 32, 32, 32));
        entities.add(npc);
    }
    
    public void restart(){
        if(resetar == true){
            resetar = false;
            spritesheet = new Spritesheet("/sprites.png");
            entities = new ArrayList<Entity>();
            enemies = new ArrayList<Enemy>();
            player = new Player(0, 0, 32, 32, spritesheet.getSprite(64, 0, 32, 32), controller);
            entities.add(player);
            world = new World(newWorld);
            gameState = "NORMAL";
        }
    }

    public void cursorIcon(){
        Image icon = null;
        try {
            icon = ImageIO.read(getClass().getResource("/icon.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image imagem = toolkit.getImage(getClass().getResource("/icon.png"));
        Cursor c = toolkit.createCustomCursor(imagem, new Point(10,0), "img");
        window.setCursor(c);
        window.setIconImage(icon);
    }
    

    public void createWindow(){
        setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize()));
        //setPreferredSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
        setMinimumSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
        setMaximumSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));

        window = new JFrame("Jogo Gosminha");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLayout(new BorderLayout());
        window.add(this, BorderLayout.CENTER);
        window.setUndecorated(true);
        cursorIcon();
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
        for(int i = 0; i < spittle.size(); i++){
            spittle.get(i).tick();
        }
    }

    public void loopRender(Graphics g){
        world.render(g);
        Collections.sort(entities, Entity.nodeSorter);
        for(int i = 0; i < entities.size(); i++){
            Entity entity = entities.get(i);
            entity.render(g);
        }
        for(int i = 0; i < spittle.size(); i++){
            spittle.get(i).render(g);
        }
        ui.render(g);
    }

    @Override
    public void run() {
        requestFocus();
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
