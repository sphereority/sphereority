package common;

import java.awt.Graphics2D;

/**
 * This represents a location with possible tracking capabilities
 * @author dvanhumb
 *
 */
public class WeightedPosition extends Actor implements Constants
{
	protected WeightedPosition target;
	
	public WeightedPosition()
	{
		this(0, 0);
	}
	
	public WeightedPosition(float x, float y)
	{
		setPosition(x, y);
	}
	
	public float getX()
	{
		return position.x;
	}
	
	public float getY()
	{
		return position.y;
	}
	
	public void setPosition(float x, float y)
	{
		position.x = x;
		position.y = y;
	}
	
	public void moveBy(float x, float y)
	{
		position.x += x;
		position.y += y;
	}
	
	public float getSpeedX()
	{
		return velocity.x;
	}
	
	public float getSpeedY()
	{
		return velocity.y;
	}
	
	public void accelerate(float x, float y)
	{
		velocity.x += x;
		velocity.y += y;
		checkSpeed();
	}
	
	protected float checkSpeed()
	{
		float speed = velocity.getMagnitude();
		if (speed > MAXIMUM_SPEED)
		{
			velocity.x = MAXIMUM_SPEED * velocity.x / speed;
			velocity.y = MAXIMUM_SPEED * velocity.y / speed;
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
			float dx = target.getX() - position.x;
			float dy = target.getY() - position.y;
			float speed = speedOf(dx, dy);
			
			if (speed > 1)
			{
				dx /= speed;
				dy /= speed;
			}
			
			if (speed > 0.1f)
			{
				if (velocity.getMagnitude() < speed)
				{
					velocity.x += dx * TRACKING_SPEED;
					velocity.y += dy * TRACKING_SPEED;
				}
				else
				{
					velocity.x -= dx * TRACKING_SPEED;
					velocity.y -= dy * TRACKING_SPEED;
				}
			}
		}
		
		// Apply some friction to our motion
		velocity.x *= FRICTION_COEFFICIENT;
		velocity.y *= FRICTION_COEFFICIENT;
		
		// Check our speed to see if it's too fast
		if (checkSpeed() < 0.01f) return false;
		
		// Actually apply the speed to our position
		position.x += velocity.x * dTime;
		position.y += velocity.y * dTime;
		
		return true;
	}
	
	
	public void draw(Graphics2D g, float scale)
	{
		/*
		 * We don't actually draw anything here, as this still doesn't have
		 * a graphical representation, but still needs this method so as to
		 * be a non-abstract class.
		 */
	}
	
	/**
	 * Get the total speed of a particular vector 
	 * @param a		The first part of the vector
	 * @param b		The second part of the vector
	 * @return	The total speed of the vector
	 */
	public static float speedOf(float a, float b)
	{
		return (float)Math.sqrt(a*a + b*b);
	}
	
	public void collision(Actor a) {}
}
