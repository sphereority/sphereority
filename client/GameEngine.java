package	client;

import common.*;
import client.audio.*;
import java.awt.BorderLayout;
import java.awt.event.*;
import javax.swing.JFrame;
import javax.swing.Timer;
import java.awt.geom.Rectangle2D;
import java.util.Vector;
import java.util.Random;

/**
 * This class describes the game loop for this game
 * @author smaboshe
 *
 */
public class GameEngine implements Constants, ActionListener {
	public static final Random rand = new Random();
	
	public boolean gameOver;
	public Map gameMap;
	public ClientViewArea gameViewArea;
	public LocalPlayer localPlayer;
	public InputListener localInputListener;
	
	// Actor lists and sub-lists
	public Vector<Actor> actorList;
	public Vector<Stone> stoneList;
	public Vector<Player> playerList;
	public Vector<Projectile> bulletList;
	
	public long lastTime;
	public Timer timer;
	
	public Vector<MapChangeListener> mapListeners;
	
	// Sound stuff
	public GameSoundSystem soundSystem;
	public SoundEffect soundBump;


	// CONSTRUCTORS
	public GameEngine(Map m) {
		gameOver = false;
		gameMap = m;
		mapListeners = new Vector<MapChangeListener>();
		
		gameViewArea = new ClientViewArea(this);
		addMapListener(gameViewArea);
		
		localInputListener = new InputListener();
		localPlayer = new LocalPlayer(localInputListener);
		placePlayer(localPlayer);
		gameViewArea.setLocalPlayer(localPlayer);
		
		actorList = new Vector<Actor>();
		actorList.add(localPlayer);
		stoneList = new Vector<Stone>();
		playerList = new Vector<Player>();
		playerList.add(localPlayer);
		bulletList = new Vector<Projectile>();
		
		triggerMapListeners();
		
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
		gameOver = true;
		if (timer != null)
			timer.stop();
	}
	
	public void gameStep()
	{
		checkCollisions();
		updateWorld();
	}
	
	public void play() {
		initialize();
		
		timer = new Timer(10, this);
		timer.start();
		
//		while (!isGameOver()) {
//			gameStep();
//			
//			try { Thread.sleep(10); }
//			catch (InterruptedException er) { }
//		}
	}
	
	protected void triggerMapListeners()
	{
		for (MapChangeListener mcl : mapListeners)
			mcl.mapChanged(gameMap);
	}
	
	public void addMapListener(MapChangeListener listener)
	{
		mapListeners.add(listener);
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
				{
					stoneList.add(new Stone(x, y));
					actorList.add(stoneList.get(stoneList.size() - 1));
				}
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
		Rectangle2D bounds1, bounds2;
		Actor actor1, actor2;
		
		// Check players against stones
		for (int i=0; i < stoneList.size(); i ++)
		{
			actor1 = stoneList.get(i);
			bounds1 = actor1.getBounds();
			
			for (int j=0; j < playerList.size(); j ++)
			{
				actor2 = playerList.get(j);
				bounds2 = actor2.getBounds();
				
				if (bounds1.intersects(bounds2))
				{
					actor1.collision(actor2);
					actor2.collision(actor1);
				}
			}
		} // end check players against stones
		
		// Check bullets against players and stones
		for (int i=0; i < bulletList.size(); i ++)
		{
			actor1 = bulletList.get(i);
			bounds1 = actor1.getBounds();
			
			for (int j=0; j < playerList.size(); j ++)
			{
				actor2 = playerList.get(j);
				bounds2 = actor2.getBounds();
				
				if (bounds1.intersects(bounds2))
				{
					actor1.collision(actor2);
					actor2.collision(actor1);
				}
			}
			
			for (int j=0; j < stoneList.size(); j ++)
			{
				actor2 = stoneList.get(j);
				bounds2 = actor2.getBounds();
				
				if (bounds1.intersects(bounds2))
				{
					actor1.collision(actor2);
					actor2.collision(actor1);
				}
			}
		} // end check bullets against players and stones
		
		//Vector actorList = this.gameViewArea.actorList;
//		Rectangle2D playerBounds = this.localPlayer.getBounds();
//		for (int i = 0; i < actorList.size(); i = i + 1) {
//			Actor actor1 = actorList.get(i);
//			if (actor1 instanceof TrackingObject) continue;
//			
//			Rectangle2D bound1 = actor1.getBounds();
//			if (bound1.intersects(playerBounds)) {
//				this.localPlayer.collision(actor1);
//				actor1.collision(this.localPlayer);
//			}
//			for (int j = i + 1; j < actorList.size(); j = j + 1) {
//				Actor actor2 = actorList.get(j);
//				if (actor2 instanceof TrackingObject) continue;
//				
//				Rectangle2D bound2 = actor2.getBounds();
//				if (bound1.intersects(bound2)) {
//					actor1.collision(actor2);
//					actor2.collision(actor1);
//				}
//			}
//		}
	}
	
	public void updateWorld() {
		long thisTime = System.currentTimeMillis();
		float dTime = 0.001f*(thisTime - lastTime);
		boolean repaint = false;
		
		for (Actor a : actorList)
		{
			if (!(a instanceof Stone) && a.animate(dTime))
				repaint = true;
		}
		
		lastTime = thisTime;
		if (repaint)
			gameViewArea.repaint();
	}
	
	public void actionPerformed(ActionEvent e)
	{
		gameStep();
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