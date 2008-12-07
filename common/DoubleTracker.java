package common;

import java.awt.Graphics2D;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DoubleTracker extends Actor {
	public static Logger logger = Logger.getLogger(CLIENT_LOGGER_NAME);

	protected Actor firstActor, secondActor;
	
	public DoubleTracker()
	{
		
	}
	
	public DoubleTracker(Actor first, Actor second)
	{
		firstActor = first;
		secondActor = second;
		
		animate(0, 0);
	}
	
	public boolean animate(float dTime, float currentTime)
	{
		if (firstActor == null || firstActor.position == null)
		{
			if (secondActor == null || secondActor.position == null)
			{
				System.out.println("common.DoubleTracker.animate(): Both actors are null!");
				return false;
			}
			else
			{
				System.out.println("common.DoubleTracker.animate(): First actor is null.");
				position.x = secondActor.position.x;
				position.y = secondActor.position.y;
			}
		}
		else // Both firstActor and its position are NOT null
		{
			if (secondActor == null || secondActor.position == null)
			{
				System.out.println("common.DoubleTracker.animate(): Second actor is null.");
				position.x = firstActor.position.x;
				position.y = firstActor.position.y;
			}
			else
			{
				position.x = (firstActor.position.x + secondActor.position.x) / 2;
				position.y = (firstActor.position.y + secondActor.position.y) / 2;
			}
		}
		
		// We only return false because if either of the actors we're tracking move, they'll return true anyway
		return false;
	}
	
	public void collision(Actor a)
	{
		// We don't collide with anything
	}
	
	public void draw(Graphics2D g, float scale)
	{
		// We don't have a graphical representation
	}

}
