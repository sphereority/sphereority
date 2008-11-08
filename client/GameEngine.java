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

	
	// SETTERS
	
	
	// OPERATIONS
	public void play() {
		String title = "Game Engine Test";
		System.out.println(title);
	}
}
