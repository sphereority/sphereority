package	client;

import common.*;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import java.awt.geom.Rectangle2D;
import java.util.Vector;

/**
 * This class describes the game loop for this game
 * @author smaboshe
 *
 */
public class GameEngine {
	public boolean gameOver;
	public Map gameMap;
	public ClientViewArea gameViewArea;
	public LocalPlayer localPlayer;
	public InputListener localInputListener;
	
	public Vector<Actor> actorList;
	public long lastTime;


	// CONSTRUCTORS
	public GameEngine(Map m) {
		this.gameOver = false;
		this.gameMap = m;
		this.gameViewArea = new ClientViewArea();
		
		this.gameViewArea.setMap(this.gameMap);
		
		this.localInputListener = new InputListener();
		this.localPlayer = new LocalPlayer(this.localInputListener);
		this.localPlayer.setPosition(0.5f, 0.5f);
		this.gameViewArea.setLocalPlayer(this.localPlayer);
		
		actorList = new Vector<Actor>();
		actorList.add(localPlayer);
	}
	
	
	// GETTERS
	public LocalPlayer getLocalPlayer() {
		return this.localPlayer;
	}
	
	public Map getGameMap() {
		return this.gameMap;
	}

	
	// SETTERS
	public void setLocalPlayer(LocalPlayer p) {
		this.localPlayer = p;
	}
	
	public void setGameMap(Map m) {
		this.gameMap = m;
	}
	
	
	// OPERATIONS
	public boolean isGameOver() {
		return this.gameOver;
	}
	
	public void gameOver() {
		this.gameOver = true;
	}
	
	public void play() {
		initialize();
		
		while (!isGameOver()) {
			checkCollisions();
			updateWorld();
			
			//Thread.yield();
			try { Thread.sleep(10); }
			catch (InterruptedException er) { }
		}
	}
	
	public void initialize() {
		String title = "Game Engine Test";
		System.out.println(title);
		
		// Set up game window
		JFrame window = new JFrame(title);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		gameViewArea.setActorList(actorList);
		
		// TEMP: Set up temporary stones to test collision detection
		for (int i = 0; i < 10; i = i + 1) {
			Stone s = new Stone(new Position(3.5f, i * 3 + 0.5f));
			//gameViewArea.actorList.add(s);
			actorList.add(s);
		}
				
		window.getContentPane().add(this.gameViewArea, BorderLayout.CENTER);
		window.addKeyListener(this.gameViewArea);
		
		localInputListener.attachListeners(window);
		//window.addKeyListener(this.localInputListener);
		//window.addMouseListener(this.localInputListener);
		//window.addMouseMotionListener(this.localInputListener);
		
		window.pack();
		window.setLocationRelativeTo(null);
		window.setVisible(true);
		
		lastTime = System.currentTimeMillis();
	}
	
	public void checkCollisions() {
		// Environment, Player and projectile collision code goes here
		Rectangle2D playerBounds = this.localPlayer.getBounds();
		//Vector actorList = this.gameViewArea.actorList;
		for (int i = 0; i < actorList.size(); i = i + 1) {
			Actor actor1 = actorList.get(i);
			if (actor1 instanceof TrackingObject) continue;
			
			Rectangle2D bound1 = actor1.getBounds();
			if (bound1.intersects(playerBounds)) {
				this.localPlayer.collision(actor1);
				actor1.collision(this.localPlayer);
			}
			for (int j = i + 1; j < actorList.size(); j = j + 1) {
				Actor actor2 = actorList.get(j);
				if (actor2 instanceof TrackingObject) continue;
				
				Rectangle2D bound2 = actor2.getBounds();
				if (bound1.intersects(bound2)) {
					actor1.collision(actor2);
					actor2.collision(actor1);
				}
			}
		}
	}
	
	public void updateWorld() {
		long thisTime = System.currentTimeMillis();
		float dTime = 0.001f*(thisTime - lastTime);
		boolean repaint = false;
		
		for (Actor a : actorList)
		{
			if (a.animate(dTime))
				repaint = true;
		}
		
		lastTime = thisTime;
		if (repaint)
			gameViewArea.repaint();
	}
}