package	client;

import common.Map;

/**
 * This class describes the game loop for this game
 * @author smaboshe
 *
 */
public class GameEngine {
	public boolean gameOver;
	public Map gameMap;

	// CONSTRUCTORS
	public GameEngine(Map m) {
		this.gameOver = false;
		this.gameMap = m;
	}
	
	// GETTERS
	public Map getMap() {
		return this.gameMap;
	}

	
	// SETTERS
	public void setMap(Map m) {
		this.gameMap = m;
	}
	
	
	// OPERATIONS
	public void play() {
		String title = "Game Engine Test";
		System.out.println(title);
	}
}
