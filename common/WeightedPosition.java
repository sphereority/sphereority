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
		return 0;//speed_x;
	}
	
	public float getSpeedY()
	{
		return 0;//speed_y;
	}
	
	public void accelerate(float x, float y)
	{
		//speed_x += x;
		//speed_y += y;
		checkSpeed();
	}
	
	protected float checkSpeed()
	{
		//float speed = (float) Math.sqrt(speed_x * speed_x + speed_y * speed_y);
		/*if (speed > MAXIMUM_SPEED)
		{
			speed_x = MAXIMUM_SPEED * speed_x / speed;
			speed_y = MAXIMUM_SPEED * speed_y / speed;
			return MAXIMUM_SPEED;
		}*/
		return 0;//speed;
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
		/*
		// If we're tracking something/someone
		if (target != null)
		{
			float dx = target.getX() - position.x;
			float dy = target.getY() - pos_y;
			float speed = speedOf(dx, dy);
			
			if (speed > 1)
			{
				dx /= speed;
				dy /= speed;
			}
			
			if (speed > 0.1f)
			{
				if (speedOf(speed_x, speed_y) < speed)
				{
					//speed_x += dx * TRACKING_SPEED;
					//speed_y += dy * TRACKING_SPEED;
				}
				else
				{
					//speed_x -= dx * TRACKING_SPEED;
					//speed_y -= dy * TRACKING_SPEED;
				}
			}
		}
		
		// Apply some friction to our motion
		//speed_x *= FRICTION_COEFFICIENT;
		//speed_y *= FRICTION_COEFFICIENT;
		
		// Check our speed to see if it's too fast
		if (checkSpeed() < 0.01f) return false;
		
		// Actually apply the speed to our position
		//position.x += speed_x * dTime;
		//position.y += speed_y * dTime;
		*/
		return false;
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
}
