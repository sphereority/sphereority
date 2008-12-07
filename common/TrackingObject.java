package	common;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Describes an object that follows an Actor
 *
 */
public class TrackingObject extends WeightedPosition {
	public static Logger logger = Logger.getLogger(CLIENT_LOGGER_NAME);

	protected Actor target;
	
	public TrackingObject()
	{
		this(0, 0);
	}
	
	public TrackingObject(Actor a)
	{
		this(a.getPosition());
		setTarget(a);
	}
	
	public TrackingObject(float x, float y)
	{
		super();
		setPosition(x, y);
		width = height = 0; // This doesn't bump into anything
	}
	
	public TrackingObject(Position p)
	{
		// Copy the position so we're not actually following their position exactly
		// Possible bug: Position objects are not immutable!
		position = new Position(p);
	}

	public void setTarget(Actor target)
	{
		this.target = target;
	}
	
	public Actor getTarget()
	{
		return target;
	}
	
	public boolean animate(float dTime, float currentTime)
	{
		// If we're tracking something/someone
		if (target != null)
		{
			Position delta = target.position.subtract(position);
			
			if (delta.getMagnitude() < 0.01f && target.velocity.getMagnitude() < 0.05f)
			{
				velocity.x = velocity.y = 0;
				position.x = target.getX();
				position.y = target.getY();
				
				return false;
			}
			
			velocity.scale(1 - ((1 - FRICTION_COEFFICIENT) * dTime));
			
			if (velocity.getMagnitude() > 0.1f)
				velocity.setDirection(delta);
			
			if (delta.getMagnitude() > velocity.getMagnitude())
				velocity.move(delta, TRACKING_SPEED * dTime / delta.getMagnitude());
			else
				velocity.move(delta, -TRACKING_SPEED * dTime / delta.getMagnitude());
		} // end if we have a target
		
		velocity.checkLength();
		
		if (velocity.getMagnitude() < 0.01f)
			return false;
		
		position.move(velocity, dTime);
		
		return true;
	}
}
