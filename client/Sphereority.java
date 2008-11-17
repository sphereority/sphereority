package	client;

import common.*;

/**
 * This is the main client application
 * @author smaboshe
 */
public class Sphereority extends Thread {
	private GameEngine game;
	
	public Sphereority(GameEngine game)
	{
		this.game = game;
	}
	
	public static void main(String[] args) {
		//GameEngine game = new GameEngine(new Map("widefield"));
		//GameEngine game = new GameEngine(new Map("round"));
		//GameEngine game = new GameEngine(new Map("random_1"));
		//GameEngine game = new GameEngine(new Map("circles"));
		GameEngine game = new GameEngine(new Map("mercury"));
		Sphereority s = new Sphereority(game);
		s.start();
	}
	
	public void run()
	{
		game.play();
	}
}
