package	client;

import common.*;
import client.audio.*;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import java.awt.geom.Rectangle2D;
import java.util.Vector;
import java.util.Random;

/**
 * This class describes the game loop for this game
 * @author smaboshe
 *
 */
public class GameEngine implements Constants {
	public static final Random rand = new Random();
	
	public boolean gameOver;
	public Map gameMap;
	public ClientViewArea gameViewArea;
	public LocalPlayer localPlayer;
	public InputListener localInputListener;
	
	public Vector<Actor> actorList;
	public long lastTime;
	public GameSoundSystem soundSystem;
	public SoundEffect soundBump;


	// CONSTRUCTORS
	public GameEngine(Map m) {
		gameOver = false;
		gameMap = m;
		gameViewArea = new ClientViewArea();
		
		gameViewArea.setMap(gameMap);
		
		localInputListener = new InputListener();
		localPlayer = new LocalPlayer(localInputListener);
		placePlayer(localPlayer);
		gameViewArea.setLocalPlayer(localPlayer);
		
		actorList = new Vector<Actor>();
		actorList.add(localPlayer);
		
		// Sound engine stuff:
		soundSystem = new GameSoundSystem();
		soundBump = soundSystem.loadSoundEffect(SOUND_BUMP);
	}
	
	
	// GETTERS
	public LocalPlayer getLocalPlayer() {
		return this.localPlayer;
	}
	
	public Map getGameMap() {
		return this.gameMap;
	}

	
	// SETTERS
	public void setLocalPlayer(LocalPlayer p) {
		this.localPlayer = p;
	}
	
	public void setGameMap(Map m) {
		this.gameMap = m;
	}
	
	
	// OPERATIONS
	public boolean isGameOver() {
		return this.gameOver;
	}
	
	public void gameOver() {
		this.gameOver = true;
	}
	
	public void play() {
		initialize();
		
		while (!isGameOver()) {
			checkCollisions();
			updateWorld();
			
			//Thread.yield();
			try { Thread.sleep(10); }
			catch (InterruptedException er) { }
		}
	}
	
	public void initialize() {
		String title = CLIENT_WINDOW_NAME;
		System.out.println(title);
		
		// Set up game window
		JFrame window = new JFrame(title);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		gameViewArea.setActorList(actorList);
		
		// Copy the map as a bunch of Stones
		for (int x=0; x < gameMap.getXSize(); x++)
			for (int y=0; y < gameMap.getYSize(); y++)
			{
				if (gameMap.isWall(x, y))
					actorList.add(new Stone(x, y));
			}
				
		window.getContentPane().add(this.gameViewArea, BorderLayout.CENTER);
		window.addKeyListener(this.gameViewArea);
		
		localInputListener.attachListeners(window);
		
		window.pack();
		window.setLocationRelativeTo(null);
		window.setVisible(true);
		
		lastTime = System.currentTimeMillis();
	}
	
	public void checkCollisions() {
		// Environment, Player and projectile collision code goes here
		Rectangle2D playerBounds = this.localPlayer.getBounds();
		//Vector actorList = this.gameViewArea.actorList;
		for (int i = 0; i < actorList.size(); i = i + 1) {
			Actor actor1 = actorList.get(i);
			if (actor1 instanceof TrackingObject) continue;
			
			Rectangle2D bound1 = actor1.getBounds();
			if (bound1.intersects(playerBounds)) {
				this.localPlayer.collision(actor1);
				actor1.collision(this.localPlayer);
			}
			for (int j = i + 1; j < actorList.size(); j = j + 1) {
				Actor actor2 = actorList.get(j);
				if (actor2 instanceof TrackingObject) continue;
				
				Rectangle2D bound2 = actor2.getBounds();
				if (bound1.intersects(bound2)) {
					actor1.collision(actor2);
					actor2.collision(actor1);
				}
			}
		}
	}
	
	public void updateWorld() {
		long thisTime = System.currentTimeMillis();
		float dTime = 0.001f*(thisTime - lastTime);
		boolean repaint = false;
		
		for (Actor a : actorList)
		{
			if (a.animate(dTime))
				repaint = true;
		}
		
		lastTime = thisTime;
		if (repaint)
			gameViewArea.repaint();
	}
	
	public void placePlayer(Player p)
	{
		Vector<SpawnPoint> spawnPoints = gameMap.getSpawnPoints();
		
		if (spawnPoints == null || spawnPoints.size() == 0)
		{
			final int width = gameMap.getXSize(), height = gameMap.getYSize();
			int x = rand.nextInt(width), y = rand.nextInt(height);
			
			while (gameMap.isWall(x, y))
			{
				x = rand.nextInt(width);
				y = rand.nextInt(height);
			}
			
			p.setPosition(x + 0.5f, y + 0.5f);
		}
		else
		{
			p.setPosition(spawnPoints.get(rand.nextInt(spawnPoints.size())).getPosition());
		}
	}
	
	/* ********************************************* *
	 * These method(s) are for playing sound effects *
	 * ********************************************* */
	/**
	 * Plays a bump sound effect at the specified volume. If the sound is already playing at a lower volume, it is stopped and restarted at the louder volume, otherwise nothing happens.
	 * @param volume	The volume at which to play.
	 */
	public void playBump(float volume)
	{
		playSound(volume, soundBump);
	}
	
	/**
	 * A slightly more generic sound playing method so we don't have to duplicate code all over the place.
	 * This actually handles playing or not playing the sound.
	 * @param volume
	 * @param sound
	 */
	private void playSound(float volume, SoundEffect sound)
	{
		if (sound.isPlaying())
		{
			if (sound.getVolume() < volume)
				sound.stop();
			else
				return;
		}
		
		sound.setVolume(volume);
		sound.play();
	}
}