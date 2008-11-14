package client;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import common.*;

/**
 * This class describes a human player who has local access to the keyboard and
 * mouse
 * 
 * @author smaboshe
 * 
 */
public class LocalPlayer extends Player
{
	protected InputListener inputDevice;
	private int bump_count;
	protected Rectangle2D bounds = null;
	
	/**
	 * Due to the requirement that this type of Player needs information about
	 * key events and mouse motion, we <u>need</u> an object with information
	 * about the currently-pressed keys.
	 * 
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
		if (inputDevice.isLeftKeyPressed()) accelerate(-PLAYER_ACCELERATION*dTime, 0);
		if (inputDevice.isRightKeyPressed()) accelerate(PLAYER_ACCELERATION*dTime, 0);
		if (inputDevice.isUpKeyPressed()) accelerate(0, -PLAYER_ACCELERATION*dTime);
		if (inputDevice.isDownKeyPressed()) accelerate(0, PLAYER_ACCELERATION*dTime);
		bump_count--;
		
		boolean result = super.animate(dTime);
		if (result && bounds != null)
		{
			bounds.setRect(position.getX() - 0.5f*width, position.getY() - 0.5f*height, width, height); 
		}
		return result;
	}
	
	public void draw(Graphics2D g, float scale)
	{
		super.draw(g, scale);
		if (bump_count > 0)
		{
			g.setColor(Color.yellow);
			g.drawRect(Math.round((getX() - 0.5f * width) * scale), Math.round((getY() - 0.5f * height) * scale), Math.round(width * scale), Math.round(height * scale));
		}
	}
	
	public void collision(Actor a)
	{
		super.collision(a);
		if (a instanceof Stone)
		{
			System.out.println("You hit a " + a.getClass().getName() + " at " + System.currentTimeMillis());
		}
		if (a instanceof Stone)
		{
			bump_count = 2;
			Position difference = a.getPosition().subtract(position);
			if (Math.abs(difference.getX()) > Math.abs(difference.getY()))
			{
				if ((difference.getX() > 0.01f && velocity.getX() > 0.01f) || (difference.getX() < -0.01f && velocity.getX() < -0.01f))
					velocity.bounceX();
			}
			else if (Math.abs(difference.getY()) > Math.abs(difference.getX()))
			{
				if ((difference.getY() > 0.01f && velocity.getY() > 0.01f) || (difference.getY() < -0.01f && velocity.getY() < -0.01f))
					velocity.bounceY();
			}
		}
	} // end collision()
	
	public Rectangle2D getBounds()
	{
		if (bounds == null)
			bounds = super.getBounds();
		
		return bounds;
	}

	public void collideDown()
	{
		if (getSpeedY() > 0)
		{
			velocity.bounceY();
			velocity.setY(velocity.getY() - BUMP_FORCE);
			timeSinceLastSound = 0;
		}
	}

	public void collideLeft()
	{
		if (getSpeedX() < 0)
		{
			velocity.bounceX();
			velocity.setX(velocity.getX() + BUMP_FORCE);
			timeSinceLastSound = 0;
		}
	}

	public void collideRight()
	{
		if (getSpeedX() > 0)
		{
			velocity.bounceX();
			velocity.setX(velocity.getX() - BUMP_FORCE);
			timeSinceLastSound = 0;
		}
	}

	public void collideUp()
	{
		if (getSpeedY() < 0)
		{
			velocity.bounceY();
			velocity.setY(velocity.getY() + BUMP_FORCE);
			timeSinceLastSound = 0;
		}
	}
} // end class LocalPlayer
