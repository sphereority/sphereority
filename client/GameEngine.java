package	client;

/**
 * This class describes the game loop for this game
 * @author smaboshe
 *
 */
public class GameEngine {
	// INSTANCE METHODS
	private boolean gameEnded;
	private int ticks;


	// CONSTRUCTORS
	public GameEngine() {
		this.gameEnded = false;
	}
	
	// GETTERS	
	public boolean hasEnded() {
		return this.gameEnded;
	}
	
	// SETTERS
	
	
	// OPERATIONS	
	public void end() {
		gameEnded = true;
	}
}