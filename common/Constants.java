package common;

import java.awt.Color;
//import java.util.logging.Level;
//import java.util.logging.Logger;
import java.util.Random;

/**
 * These are some constants that are useful to have quickly
 * @author dvanhumb
 */

public interface Constants
{
	public static final Random RANDOM = new Random();
	
	/**
	 * Default port for TCP network listener
	 */
	public static final int DEFAULT_PORT = 44000;
     
	/**
	* Address for UDP Mutlicast
	*/
	public static final String MCAST_ADDRESS = "237.75.31.125";
	
	 /**
     * Address for players playing a game.
     */
    public static final String PLAYER_MCAST_ADDRESS = "224.1.1.15";

    /**
     * Port for multicast socket
     */
    public static final int MCAST_PORT = 44000;
	
     /**
     * Address for receiving UDP Login requests.
     */
    public static final String SERVER_ADDRESS = "224.1.1.20";

    /**
     * Port for the server socket.
     */
    public static final int SERVER_PORT = 45000;
    
    /**
     * Placeholder string while we try to figure out a player's name.
     */
    public static final String RESOLVING_NAME = "Resolving Name...";
    
	/**
	 * The fastest an object can move in units per second
	 */
	public static final float MAXIMUM_SPEED = 10;

	/**
	 * The amount of friction we have to slow motion down
	 */
	public static final float FRICTION_COEFFICIENT = 1 - 0.85f;

	/**
	 * The speed at which objects track each other (this affects the "slow parent" of objects)
	 */
	public static final float TRACKING_SPEED = 5.0f;

	/**
	 * The amount of speed you have after bumping into a wall
	 */
	public static final float STONE_REBOUND = 0.75f;

	/**
	 * The amount of speed you have after bumping into another player
	 */
	public static final float PLAYER_REBOUND = 0.5f;

	/**
	 * The speed at which a player accelerates due to keypresses
	 */
	public static final float PLAYER_ACCELERATION = 2.5f;

	/**
	 * The size of a player in world units
	 */
	public static final float PLAYER_SIZE = 0.5f;
	
	/**
	 * The length of a player's barrle in world units
	 */
	public static final float AIM_LENGTH = 0.4f;

	/**
	 * The width of the player window
	 */
	public static final int GAME_WINDOW_WIDTH = 640;

	/**
	 * The height of the player window
	 */
	public static final int GAME_WINDOW_HEIGHT = 480;

	/**
	 * The name of the folder that contains game resources
	 */
	public static final String RESOURCES_FOLDER = "resources/";

	/**
	 * The name of the file that contains the "wall bump" sound
	 */
	public static final String SOUND_BUMP = RESOURCES_FOLDER + "wall_bump.wav";
	
	/**
	 * The name of the file that contains the death sound
	 */
	public static final String SOUND_DEATH = RESOURCES_FOLDER + "explosion.wav";
	
	/**
	 * The name of the file that contains the fire sound
	 */
	public static final String SOUND_FIRE = RESOURCES_FOLDER + "photon.wav";

	/**
	 * The team ID of the first team (Spheres)
	 */
	public static final int TEAM_A = 0x11;

	/**
	 * The human-readable name of the first team (Spheres)
	 */
	public static final String TEAM_A_NAME = "Spheres";
	
	/**
	 * The colour for the Spheres team
	 */
	public static final Color TEAM_A_COLOR = Color.cyan;

	/**
	 * The colour of a stone
	 */
	public static final Color STONE_COLOR = new Color(0.6f, 0.2f, 0.1f);

	/**
	 * The width of a stone
	 */
	public static final int STONE_WIDTH = 1;

	/**
	 * The height of a stone
	 */
	public static final int STONE_HEIGHT = 1;


	/**
	 * The team ID of the second team (Orbs)
	 */
	public static final int TEAM_B = 0x12;

	/**
	 * The human-readable name of the second team (Orbs)
	 */
	public static final String TEAM_B_NAME = "Orbs";
	
	/**
	 * The colour for the Orbs team
	 */
	public static final Color TEAM_B_COLOR = Color.orange;
	
	/**
	 * The colour for a player with no team 
	 */
	public static final Color TEAMLESS_COLOR = Color.magenta;

	/**
	 * Default height of an actor in world units
	 */
	public static final float DEFAULT_ACTOR_HEIGHT = 0.5f;

	/**
	 * Default weight of an actor in world units
	 */
	public static final float DEFAULT_ACTOR_WIDTH = 0.5f;


	/**
	 * Default weight of an actor in world units
	 */
	public static final float DEFAULT_ACTOR_WEIGHT = 0.25f;


	/**
	 * Default health for an actor
	 */
	public static final int DEFAULT_ACTOR_HEALTH = 1000;

	/**
	 * Mininum health for an actor (below this an actor dies)
	 */
	public static final int MINIMUM_ACTOR_HEALTH = 1;

	/**
	 * Maximum health for an actor
	 */
	public static final int MAXIMUM_ACTOR_HEALTH = 2000;
	
	/**
	 * The speed at which a bullet travels
	 */
	public static final float BULLET_SPEED = 15;
	
	public static final String CLIENT_VERSION = "v0.5 beta";

	/**
	 * The name of the client application window
	 */
	public static final String CLIENT_WINDOW_NAME = "Sphereority" + " - " + CLIENT_VERSION;
	
	/**
	 * The amount of time between game steps
	 */
	public static final int TIMER_TICK = 50;
	
	/**
	 * The amount of time (in seconds) that a player shows up for after they fire
	 */
	public static final float BLIP_TIME = 2.0f;
	
	/**
	 * The amount to shove the player when they bump into a wall
	 * WARNING: Don't tweak this any lower than 0.175f, as then you can push your way through walls!
	 */
	public static final float BUMP_FORCE = 0.2f;
	
	/**
	 * The number of copies of the radar widget that can be displayed either horizontally or vertically
	 */
	public static final int RADAR_SIZE = 3;
	
	/**
	 * The amount of time in seconds that must pass between shots fired
	 */
	public static final float RELOAD_TIME = 0.5f;
	
	
	public static String LOG_FOLDER = "logs/";

	public static String CLIENT_LOG_FILE_NAME = "client." + System.currentTimeMillis() + ".log";
	public static String CLIENT_LOG_PATH = LOG_FOLDER + CLIENT_LOG_FILE_NAME;
	public static String CLIENT_LOGGER_NAME = "Sphereority Client";

	public static String SERVER_LOG_FILE_NAME = "server." + System.currentTimeMillis() + ".log";
	public static String SERVER_LOG_PATH = LOG_FOLDER + SERVER_LOG_FILE_NAME;
	public static String SERVER_LOGGER_NAME = "Sphereority Server";
}
