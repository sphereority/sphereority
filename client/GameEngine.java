package	client;

import common.Map;
import javax.swing.JFrame;

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


	// CONSTRUCTORS
	public GameEngine(Map m) {
		this.gameOver = false;
		this.gameMap = m;
		this.gameViewArea = new ClientViewArea();
	}
	
	
	// GETTERS
	public Player getLocalPlayer() {
		return this.localPlayer;
	}
	
	public Map getGameMap() {
		return this.gameMap;
	}

	
	// SETTERS
	public void setLocalPlayer(Player p) {
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
		String title = "Game Engine Test";
		System.out.println(title);
		
		// Set up game window
		JFrame window = new JFrame(title);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
	}
}
