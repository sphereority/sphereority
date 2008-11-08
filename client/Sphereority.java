package	client;

/**
 * This is the main client application
 * @author smaboshe
 *
 */

import common.Map;

public class Sphereority {
	public static void main(String[] args) {
		GameEngine game = new GameEngine(new Map());
		game.play();
	}
}
