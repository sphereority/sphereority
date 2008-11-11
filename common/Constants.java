package common;

import java.awt.Color;

/**
 * These are some constants that are useful to have quickly
 * @author dvanhumb
 */
public interface Constants
{
	/**
	 * The fastest an object can move in units per second
	 */
	public static final float MAXIMUM_SPEED = 25;

	/**
	 * The amount of friction we have to slow motion down
	 */
	public static final float FRICTION_COEFFICIENT = 1 - 0.4f;

	/**
	 * The speed at which objects track each other (this affects the "slow parent" of objects)
	 */
	public static final float TRACKING_SPEED = 2.0f;

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
	public static final float PLAYER_ACCELERATION = 0.01f;

	/**
	 * The size of a player in world units
	 */
	public static final float PLAYER_SIZE = 0.5f;

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
	public static final Color STONE_COLOR = Color.gray;

	/**
	 * The width of a stone
	 */
	public static final int STONE_WIDTH = 50;

	/**
	 * The height of a stone
	 */
	public static final int STONE_HEIGHT = 50;


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
	 * 
	 */
	public static final float BULLET_SPEED = 20;
}
