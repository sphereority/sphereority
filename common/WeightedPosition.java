package common;

public class WeightedPosition implements Constants
{
  protected float pos_x, pos_y;
  protected float speed_x, speed_y;
  
  protected WeightedPosition target;
  
  public WeightedPosition()
  {
    this(0, 0);
  }
  
  public WeightedPosition(float x, float y)
  {
    setPosition(x, y);
    speed_x = speed_y = 0;
  }
  
  public float getX()
  {
    return pos_x;
  }

  
  public float getY()
  {
    return pos_y;
  }
  
  public void setPosition(float x, float y)
  {
    pos_x = x;
    pos_y = y;
  }
  
  public void moveBy(float x, float y)
  {
    pos_x += x;
    pos_y += y;
  }
  
  public float getSpeedX()
  {
    return speed_x;
  }
  
  public float getSpeedY()
  {
    return speed_y;
  }
  
  public void accelerate(float x, float y)
  {
    speed_x += x;
    speed_y += y;
    checkSpeed();
  }
  
  protected float checkSpeed()
  {
    float speed = (float)Math.sqrt(speed_x*speed_x + speed_y*speed_y);
    if (speed > MAXIMUM_SPEED)
    {
      speed_x = MAXIMUM_SPEED * speed_x / speed;
      speed_y = MAXIMUM_SPEED * speed_y / speed;
      return MAXIMUM_SPEED;
    }
    return speed;
  }
  
  /* ******************************************** *
   * This section is about tracking other objects *
   * This may be helpful for AI stuff             *
   * ******************************************** */
  
  public void setTarget(WeightedPosition target)
  {
    this.target = target;
  }
  
  public WeightedPosition getTarget()
  {
    return target;
  }
  
  /**
   * This method handles moving the object around
   */
  public boolean animate(float dTime)
  {
    // If we're tracking something/someone
    if (target != null)
    {
      float dx = target.getX() - pos_x;
      float dy = target.getY() - pos_y;
      
      speed_x += dx*TRACKING_SPEED;
      speed_y += dy*TRACKING_SPEED;
    }
    
    // Apply some friction to our motion
    speed_x *= FRICTION_COEFFICIENT;
    speed_y *= FRICTION_COEFFICIENT;
    
    // Check our speed to see if it's too fast
    if (checkSpeed() < 0.01f)
      return false;
    
    // Actually apply the speed to our position
    pos_x += speed_x;
    pos_y += speed_y;
    
    return true;
  }
}
