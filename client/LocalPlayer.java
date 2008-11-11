package	client;

import java.awt.Color;
import java.awt.Graphics2D;
import common.*;

/**
 * This class describes a human player who has local access to the keyboard and mouse
 * @author smaboshe
 *
 */
public class LocalPlayer extends Player {
	protected InputListener inputDevice;
	
	private int bump_count;
	
	/**
	 * Due to the requirement that this type of Player needs information about
	 * key events and mouse motion, we <u>need</u> an object with information
	 * about the currently-pressed keys.
	 * @param input
	 */
	public LocalPlayer(InputListener input)
	{
		inputDevice = input;
		bump_count = 0;
		width = height = PLAYER_SIZE;
	}
	
	public boolean animate(float dTime)
	{
		if (inputDevice.isLeftKeyPressed())
			accelerate(-PLAYER_ACCELERATION, 0);
		if (inputDevice.isRightKeyPressed())
			accelerate(PLAYER_ACCELERATION, 0);
		if (inputDevice.isUpKeyPressed())
			accelerate(0, -PLAYER_ACCELERATION);
		if (inputDevice.isDownKeyPressed())
			accelerate(0, PLAYER_ACCELERATION);
		
		bump_count --;
		
		return super.animate(dTime);
	}
	
	public void draw(Graphics2D g, float scale)
	{
		super.draw(g, scale);
		if (bump_count > 0)
		{
			g.setColor(Color.yellow);
			g.drawRect(Math.round((getX() - 0.5f*width)*scale),
			           Math.round((getY() - 0.5f*height)*scale),
			           Math.round(width*scale),
			           Math.round(height*scale));
		}
	}
	
	public void collision(Actor a)
	{
		super.collision(a);
		
		if (a instanceof Stone) {
			System.out.println("You hit a " + a.getClass().getName() + " at " + System.currentTimeMillis());
		}
		
		if (a instanceof Stone)
		{
			bump_count = 2;
			
			//Position difference = position.subtract(a.getPosition());
			Position difference = a.getPosition().subtract(position);
			
			if (difference.getX() > difference.getY())
			{
				// Add check to make sure we're actually going towards the stone not away from it
				if ((difference.getX() > 0.01f && velocity.getX() > 0.01f) ||
						(difference.getX() < -0.01f && velocity.getX() < -0.01f))
				{
					velocity.bounceX();
				}
			}
			else if (difference.getY() > difference.getX())
			{
				// Add check to make sure we're actually going towards the stone not away from it
				if ((difference.getY() > 0.01f && velocity.getY() > 0.01f) ||
						(difference.getY() < -0.01f && velocity.getY() < -0.01f))
				{
					velocity.bounceY();
				}
			}
			
//			if (difference.getX() > 0.01f)
//			{
//				if (velocity.getX() > 0.01f)
//					velocity.bounceX();
//			}
//			else if (difference.getX() < -0.01f)
//			{
//				if (velocity.getX() < -0.01f)
//					velocity.bounceX();
//			}
//			if (difference.getY() > 0.01f)
//			{
//				if (velocity.getY() > 0.01f)
//					velocity.bounceY();
//			}
//			else
//			{
//				if (velocity.getY() < -0.01f)
//					velocity.bounceY();
//			}
		}
	}
}
