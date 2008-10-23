package common;

public interface Constants
{
  /**
   * The fastest an object can move in units per second
   */
  public static final float MAXIMUM_SPEED = 50;
  
  /**
   * The amount of friction we have to slow motion down
   */
  public static final float FRICTION_COEFFICIENT = 0.8f;
  
  /**
   * The speed at which objects track each other
   * (this affects the "slow parent" of objects)
   */
  public static final float TRACKING_SPEED = 0.5f;
  
  /**
   * The amount of speed you have after bumping into a wall
   */
  public static final float WALL_REBOUND = 0.75f;
  
  /**
   * The amount of speed you have after bumping into another player
   */
  public static final float PLAYER_REBOUND = 0.5f;
  
  public static final float PLAYER_ACCELERATION = 5f;
}
