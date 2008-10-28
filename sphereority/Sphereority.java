package sphereority;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferStrategy;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Sphereority extends Canvas implements Stage, KeyListener {
	
	private BufferStrategy strategy;
	private long usedTime;
	
	private SpriteCache spriteCache;
	private ArrayList actors; 
	private Player player;
	private int ticks;
	
	private boolean gameEnded = false;
	
	public Sphereority() {
		spriteCache = new SpriteCache();
		
		JFrame ventana = new JFrame("Sphereority");
		JPanel panel = (JPanel)ventana.getContentPane();
		setBounds(0,0,Constants.GAME_WINDOW_WIDTH,Constants.GAME_WINDOW_HEIGHT);
		panel.setPreferredSize(new Dimension(Constants.GAME_WINDOW_WIDTH,Constants.GAME_WINDOW_HEIGHT));
		panel.setLayout(null);
		panel.add(this);
		ventana.setBounds(0,0,Constants.GAME_WINDOW_WIDTH,Constants.GAME_WINDOW_HEIGHT);
		ventana.setVisible(true);
		ventana.addWindowListener( new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		ventana.setResizable(false);
		createBufferStrategy(2);
		strategy = getBufferStrategy();
		requestFocus();
		addKeyListener(this);
		
		setIgnoreRepaint(true);
	}
	
	public void gameOver() {
		gameEnded = true;
	}
	
	public void initializeWorld() {
		// Map setup can go here to perhaps?
    actors = new ArrayList();    

    for (int i = 0; i < 10; i++){
      Brick b = new Brick(this);
      b.setX( (int)(Math.random() * Constants.GAME_WINDOW_WIDTH) );
	    b.setY( i * 20 );
	    b.setVerticalX( (int)(Math.random() * 20-10) );
      
      actors.add(b);
    }

    player = new Player(this);
    player.setX(Constants.GAME_WINDOW_WIDTH / 2);
    player.setY((Constants.GAME_WINDOW_PLAY_HEIGHT) - 2 * player.getHeight());
	}
	
	public void addActor(Actor a) {
		actors.add(a);
	}	
	
	public Player getPlayer() {
		return player;
	}
	
	public void updateWorld() {
		int i = 0;
		while (i < actors.size()) {
			Actor m = (Actor)actors.get(i);
			if (m.isMarkedForRemoval()) {
				actors.remove(i);
			} else {
				m.act();
				i++;
			}
		}
		player.act();
	}
	
	public void checkCollisions() {
		// Environment, Player and projectile collision code goes here
		Rectangle playerBounds = player.getBounds();
		for (int i = 0; i < actors.size(); i++) {
			Actor a1 = (Actor)actors.get(i);
			Rectangle r1 = a1.getBounds();
			if (r1.intersects(playerBounds)) {
				player.collision(a1);
				a1.collision(player);
			}
		  for (int j = i+1; j < actors.size(); j++) {
		  	Actor a2 = (Actor)actors.get(j);
		  	Rectangle r2 = a2.getBounds();
		  	if (r1.intersects(r2)) {
		  		a1.collision(a2);
		  		a2.collision(a1);
		  	}
		  }
		}
	}
	
	public void paintShields(Graphics2D g) {}
    
	public void paintScore(Graphics2D g) {}
	
	public void paintAmmo(Graphics2D g) {}
	
	public void paintfps(Graphics2D g) {}
	
	public void paintChat(Graphics2D g) {}
	
	public void paintStatus(Graphics2D g) {
	  paintScore(g);
	  paintShields(g);
	  paintAmmo(g);
	  paintfps(g);	
		paintChat(g);
	}
	
	public void paintWorld() {
		Graphics2D g = (Graphics2D)strategy.getDrawGraphics();

		g.fillRect(0,0,getWidth(),getHeight());
		for (int i = 0; i < actors.size(); i++) {
			Actor m = (Actor)actors.get(i);
			m.paint(g);
		}
		player.paint(g);

	  	
	  paintStatus(g);
		strategy.show();
	}
	
	public void paintGameOver() {
		Graphics2D g = (Graphics2D)strategy.getDrawGraphics();
		g.setColor(Color.white);
		g.setFont(new Font("Arial",Font.BOLD,20));
		g.drawString("GAME OVER",Constants.GAME_WINDOW_WIDTH/2-50,Constants.GAME_WINDOW_HEIGHT/2);
		strategy.show();
	}
	
	public SpriteCache getSpriteCache() {
		return spriteCache;
	}
		
	public void keyPressed(KeyEvent e) {
		player.keyPressed(e);
	}
	
	public void keyReleased(KeyEvent e) {
		player.keyReleased(e);
	}
	public void keyTyped(KeyEvent e) {}
	
	public void play() {
		usedTime = 1000;
		ticks = 0;
		initializeWorld();
		while (isVisible() && !gameEnded) {
			ticks++;
			long startTime = System.currentTimeMillis();
			updateWorld();
			checkCollisions();
			paintWorld();
			usedTime = System.currentTimeMillis() - startTime;
			do {
			  Thread.yield();
			} while (System.currentTimeMillis() - startTime < 17);
		}
		paintGameOver();
	}
	
	public static void main(String[] args) {
		Sphereority game = new Sphereority();
		game.play();
	}
}
