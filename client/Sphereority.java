package	client;

import common.*;
import java.awt.BorderLayout;
import javax.swing.JFrame;

/**
 * This is the main client application
 * @author smaboshe
 */
public class Sphereority extends Thread implements Constants {
	private GameEngine game;
	
	public Sphereority(GameEngine game)
	{
		this.game = game;
	}
	
	public static void main(String[] args) {
		//GameEngine game = new GameEngine(new Map("circles"));
		//GameEngine game = new GameEngine(new Map("round"));
		//GameEngine game = new GameEngine(new Map("widefield"));
		//GameEngine game = new GameEngine(new Map("mercury"));
		GameEngine game = new GameEngine(new Map("random_1"));
		
		String title = CLIENT_WINDOW_NAME;
		
		// Set up the game window
		JFrame window = new JFrame(title);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		window.getContentPane().add(game.getGameViewArea(), BorderLayout.CENTER);
		window.addKeyListener(game.getGameViewArea());
		
		game.registerActionListeners(window);
		
		window.pack();
		window.setLocationRelativeTo(null);
		window.setVisible(true);
		
		
		
		
		
		
		
		Sphereority s = new Sphereority(game);
		s.start();
	}
	
	public void run()
	{
		game.play();
	}
}
