package common;

import java.awt.Graphics2D;

public class DoubleTracker extends Actor
{
	protected Actor firstActor, secondActor;
	
	public DoubleTracker()
	{
		
	}
	
	public DoubleTracker(Actor first, Actor second)
	{
		firstActor = first;
		secondActor = second;
	}
	
	public boolean animate(float dTime, float currentTime)
	{
		if (firstActor == null)
		{
			if (secondActor == null)
				return false;
			{
				position.x = secondActor.position.x;
				position.y = secondActor.position.y;
			}
		}
		else
		{
			if (secondActor == null)
			{
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
