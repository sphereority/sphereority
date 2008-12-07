package common;

import java.awt.Graphics2D;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * TODO: I'm in the process of splitting the speed stuff into two parts:
 * The velocity is the direction we want to go in
 * The velocity is the velocity we have now
 * Each step we try to move in the direction of our velocity
 */

/**
 * This represents a location with possible tracking capabilities
 * @author dvanhumb
 *
 */
public class WeightedPosition extends Actor implements Constants {
	public static Logger logger = Logger.getLogger(CLIENT_LOGGER_NAME);

	public WeightedPosition()
	{
		this(0, 0);
	}
	
	public WeightedPosition(float x, float y)
	{
		setPosition(x, y);
	}
	
	
	public void accelerate(float x, float y)
	{
		velocity.x += x;
		velocity.y += y;
		velocity.checkLength();
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
	
	/**
	 * This method handles moving the object around
	 */
	public boolean animate(float dTime, float currentTime)
	{
		// Apply some friction to our motion
		velocity.scale(1 - ((1 - FRICTION_COEFFICIENT)*dTime));
		
		// Check our speed to see if it's too fast
		// And if we aren't actually moving, say so
		if (checkSpeed() < 0.01f) return false;
		
		// Actually apply the speed to our position
		position.move(velocity, dTime);
		
		return true;
	}
	
	
	public void draw(Graphics2D g, float scale)
	{
		/*
		 * We don't actually draw anything here, as this doesn't have
		 * a graphical representation, but still needs this method so
		 * as to be a non-abstract class.
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
	
	public void collision(Actor a) {
		/*
		 * We don't need an implementation, but still need the method so
		 * as to be a non-abstract class.
		 */		
	}
}
