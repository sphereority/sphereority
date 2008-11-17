package	client;

import common.*;
import java.awt.BorderLayout;
import javax.swing.JFrame;

/**
 * This is the main client application
 * @author smaboshe
 */
public class Sphereority extends Thread implements Constants {
	public static final String[] MAP_LIST = new String[]
		{
			"circles", "mercury", "random_1", "round", "sample-map", "widefield"
		};
	private GameEngine game;
	
	public Sphereority(GameEngine game)
	{
		this.game = game;
	}
	
	public static void main(String[] args) {
		// This now grabs a random map on startup
		Map map = new Map(MAP_LIST[RANDOM.nextInt(MAP_LIST.length)]);
		GameEngine game = new GameEngine(map);
		
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
