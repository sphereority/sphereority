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
  public static final float FRICTION_COEFFICIENT = 0.25f;
  
  /**
   * The speed at which objects track each other
   * (this affects the "slow parent" of objects)
   */
  public static final float TRACKING_SPEED = 0.125f;
}
