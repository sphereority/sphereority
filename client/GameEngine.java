package	client;

import common.Map;
import common.Actor;
import common.Stone;
import common.Position;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import java.awt.Rectangle;
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
		}
	}
	
	public void initialize() {
		String title = "Game Engine Test";
		System.out.println(title);
		
		// Set up game window
		JFrame window = new JFrame(title);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// TEMP: Set up temporary stones to test collision detection
		for (int i = 0; i < 20; i = i + 1) {
			Stone s = new Stone(new Position(this.localPlayer.getX() + 70, this.localPlayer.getY() + (i * 100)));
			this.gameViewArea.actorList.add(s);
		}
				
		window.getContentPane().add(this.gameViewArea, BorderLayout.CENTER);
		window.addKeyListener(this.gameViewArea);

		window.addKeyListener(this.localInputListener);
		window.addMouseListener(this.localInputListener);
		window.addMouseMotionListener(this.localInputListener);
		
		window.pack();
		window.setLocationRelativeTo(null);
		window.setVisible(true);
	}
	
	public void checkCollisions() {
		// Environment, Player and projectile collision code goes here
		Rectangle playerBounds = this.localPlayer.getBounds();
		Vector actors = this.gameViewArea.actorList;
		for (int i = 0; i < actors.size(); i = i + 1) {
			Actor actor1 = (Actor)actors.get(i);
			Rectangle bound1 = actor1.getBounds();
			if (bound1.intersects(playerBounds)) {
				this.localPlayer.collision(actor1);
				actor1.collision(this.localPlayer);
			}
			for (int j = i + 1; j < actors.size(); j = j + 1) {
				Actor actor2 = (Actor)actors.get(j);
				Rectangle bound2 = actor2.getBounds();
				if (bound1.intersects(bound2)) {
					actor1.collision(actor2);
					actor2.collision(actor1);
				}
			}
		}
	}
	
	public void updateWorld() {
		
	}
}