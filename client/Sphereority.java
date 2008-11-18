package	client;

import common.*;
import java.awt.BorderLayout;
import javax.swing.*;

/**
 * This is the main client application
 * @author smaboshe
 */
public class Sphereority extends Thread implements Constants {
	public static final String[] MAP_LIST = new String[]
		{ "circles", "mercury", "random_1", "round", "sample-map", "widefield" };
	
	private GameEngine game;
	private static JDialog gameWindow;
	private static ClientLogonDialog loginWindow;
	
	public Sphereority(GameEngine game)
	{
		this.game = game;
	}
	
	public static void main(String[] args) {
		// This now grabs a random map on startup
		Map map = new Map(MAP_LIST[RANDOM.nextInt(MAP_LIST.length)]);
		GameEngine game = new GameEngine(map);
		
		// Set up the game gameWindow
		gameWindow = new JDialog();
		gameWindow.setTitle(CLIENT_WINDOW_NAME);
		gameWindow.setModal(true);
		
		gameWindow.getContentPane().add(game.getGameViewArea(), BorderLayout.CENTER);
		
		gameWindow.pack();
		gameWindow.setLocationRelativeTo(null);
		
		Sphereority s = new Sphereority(game);
		s.start();
		
		// Create and display the LoginDialog
		loginWindow = new ClientLogonDialog(null);
		// If the user quit the dialog, we must quit
		if (!loginWindow.show())
			System.exit(0);
		// Else play the game
		do
		{
			game.registerActionListeners(gameWindow);
			// Play the game once:
			gameWindow.setVisible(true);
			game.unregisterActionListeners(gameWindow);
			
			// Show the login dialog again
			if (!loginWindow.show())
				break;
			// If quit, don't loop
		}
		while (true);
		
		// TEMP: this is for testing only:
		gameWindow.dispose();
		
		System.exit(0);
	}
	
	public void run()
	{
		game.play();
	}
}
