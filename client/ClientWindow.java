package client;

import java.awt.*;
import java.util.Random;

import javax.swing.*;

import common.*;

public class ClientWindow extends Thread implements Constants
{
	public static final String[] MAP_LIST = new String[]
		{ "circles", "mercury", "random_1", "sample-map", "widefield" };

	private GameEngine game;
	private ClientConnection connection;
	private static JDialog gameWindow;
	private static ClientLogonDialog loginWindow;

	public ClientWindow(GameEngine engine, ClientConnection connection)
	{
		this.game = engine;
		this.connection = connection;
	}

	public void start()
	{
		game.play();
        connection.start();
	}
	
	public static void main(String[] args)
	{
		// Create and display the LoginDialog
		loginWindow = new ClientLogonDialog(null);
		
		// If the user quit the dialog, we must quit
		if (!loginWindow.show())
			System.exit(0);
		// Else play the game

		Map map;
		GameEngine game;
        ClientConnection connection;
		do
		{
			// This grabs a random map on startup
			map = new Map(MAP_LIST[RANDOM.nextInt(MAP_LIST.length)]);
            Random random = new Random();
            byte playerId = (byte) random.nextInt(6);
            System.out.println(playerId);
			game = new GameEngine(map, playerId, "User" + playerId, null);
            connection = new ClientConnection(game);
			
			// Set up the game gameWindow
			gameWindow = new JDialog();
			gameWindow.setTitle(CLIENT_WINDOW_NAME);
			gameWindow.setModal(true);
			
			gameWindow.getContentPane().add(game.getGameViewArea(), BorderLayout.CENTER);
			
			gameWindow.pack();
			gameWindow.setLocationRelativeTo(null);
			
            ClientWindow s = new ClientWindow(game,connection);

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
}
