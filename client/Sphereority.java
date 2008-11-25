package	client;

/**
 * This is the main client application
 * @author dvanhumb
 * @author smaboshe
 */

import common.*;
import java.awt.BorderLayout;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.Random;
import javax.swing.*;

public class Sphereority extends Thread implements Constants {
	public static final String[] MAP_LIST = new String[] { "circles", "mercury", "random_1", "sample-map", "widefield" };

	public static Logger logger;

	private ClientConnection connection;
	private GameEngine game;
	private static ClientLogonDialog loginWindow;
	private static JDialog gameWindow;
		
	public Sphereority(GameEngine game, ClientConnection connection) {
		this.game = game;
		this.connection = connection;
	}
	
	public static void main(String[] args) {		
		initialiseLogger();
		
		logger.info("Client initialised.");
		
		logger.severe("Major disaster");
		logger.warning("Potential problem");
		logger.info("Standard output");
		logger.config("Some config notes");
		logger.fine("Fine detail");
		logger.finer("Finer detail");
		logger.finest("Finest detail");
		
		// Create and display the LoginDialog
		//loginWindow = new ClientLogonDialog(null);
		
		// If the user quit the dialog, we must quit
		//if (!loginWindow.show())
		//	System.exit(0);
		// Else play the game

    String serverName = "localhost";
    logger.config("Server Name: " + serverName);

    if (args.length == 1) {
        serverName = args[0];
    }		

		Map map;
		GameEngine game;
    ClientConnection connection;

		do
		{
			// This grabs a random map on startup
			map = new Map(MAP_LIST[4]);
      Random random = new Random();
      byte playerId = (byte) random.nextInt(6);
      System.out.println(playerId);
      connection = new ClientConnection(null);
			game = new GameEngine(map, playerId, "User" + playerId, connection);
			connection.setGameEngine(game);
			
			// Set up the game gameWindow
			gameWindow = new JDialog();
			gameWindow.setTitle(CLIENT_WINDOW_NAME);
			gameWindow.setModal(true);
			
			gameWindow.getContentPane().add(game.getGameViewArea(), BorderLayout.CENTER);
			
			gameWindow.pack();
			gameWindow.setLocationRelativeTo(null);
			
			Sphereority s = new Sphereority(game,connection);
			try {
			    connection.loginToServer(serverName,"Bob");	
			}
			catch (Exception ex) {
			}

			s.start();
			
			game.registerActionListeners(gameWindow);
			// Play the game once:
			gameWindow.setVisible(true);
			game.unregisterActionListeners(gameWindow);
			
			// Show the login dialog again
		}
		while (loginWindow != null && loginWindow.show());
		// If quit, don't loop
		
		// TEMP: this is for testing only:
		gameWindow.dispose();
		
		System.exit(0);
	}
	
	public void run() {
		game.play();
		connection.start();
	}
	
	public static void initialiseLogger() {
		// Client application logging
		logger = Logger.getLogger(CLIENT_LOGGER_NAME);
		
		try {
		  logger.addHandler(new FileHandler(CLIENT_LOG_PATH));
		}
		catch (IOException e) {
		  e.printStackTrace();
		}
	}
}
