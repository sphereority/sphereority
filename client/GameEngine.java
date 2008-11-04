package	client;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.Timer;

/**
 * This class describes the game loop for this game
 * @author smaboshe
 *
 */
public class GameEngine implements ActionListener {
	// INSTANCE METHODS
	private boolean gameEnded;
	private long lastTickTime;
	private Timer timer;


	// CONSTRUCTORS
	public GameEngine() {
		this.gameEnded = false;
		timer = new Timer(10, this);
		timer.start();
		lastTickTime = System.currentTimeMillis();
	}
	
	// GETTERS	
	public boolean hasEnded() {
		return this.gameEnded;
	}
	
	// SETTERS
	
	
	// OPERATIONS	
	public void end() {
		gameEnded = true;
		timer.stop();
	}
	
	public void actionPerformed(ActionEvent e)
	{
		long thisTime = System.currentTimeMillis();
		float dTime = 0.001f*(thisTime - lastTickTime);
		
		
		
		lastTickTime = thisTime;
	}
}