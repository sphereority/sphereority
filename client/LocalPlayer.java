package client;

import java.awt.geom.Rectangle2D;

import common.*;
import common.messages.*;

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
	protected Rectangle2D bounds = null;
	protected float timeSinceLastShot;
	
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
		width = height = PLAYER_SIZE;
		
		timeSinceLastShot = RELOAD_TIME; 
	}
	
	public LocalPlayer(InputListener input, byte playerID, String name)
	{
		super(playerID, name);
		inputDevice = input;
		width = height = PLAYER_SIZE;
	}
	
	public void fire()
	{
		if (timeSinceLastShot < RELOAD_TIME)
			return;
		
		super.fire();
		timeSinceLastShot = 0;
		GameEngine.gameEngine.playFire(1.0f);
	}
	
	public boolean animate(float dTime, float currentTime)
	{
		timeSinceLastShot += dTime;
		
		if (inputDevice.isLeftKeyPressed())  accelerate(-PLAYER_ACCELERATION*dTime, 0);
		if (inputDevice.isRightKeyPressed()) accelerate(PLAYER_ACCELERATION*dTime, 0);
		if (inputDevice.isUpKeyPressed())    accelerate(0, -PLAYER_ACCELERATION*dTime);
		if (inputDevice.isDownKeyPressed())  accelerate(0, PLAYER_ACCELERATION*dTime);
		if (inputDevice.isButtonFiring())     fire();
		
		boolean result = super.animate(dTime, currentTime);
		if (result && bounds != null)
		{
			bounds.setRect(position.getX() - 0.5f*width, position.getY() - 0.5f*height, width, height); 
		}
		return result;
	}
	
	/**
	 * Create a player motion packet
	 * @param currentTime	The current game time
	 * @return	The motion packet
	 */
	public PlayerMotionMessage getMotionPacket(float currentTime)
	{
		return new PlayerMotionMessage((byte)getPlayerID(), getX(), getY(), getSpeedX(), getSpeedY(), currentTime);
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
			playBump();
			velocity.bounceY();
			velocity.setY(velocity.getY() - BUMP_FORCE);
			timeSinceLastSound = 0;
		}
	}

	public void collideLeft()
	{
		if (getSpeedX() < 0)
		{
			playBump();
			velocity.bounceX();
			velocity.setX(velocity.getX() + BUMP_FORCE);
			timeSinceLastSound = 0;
		}
	}

	public void collideRight()
	{
		if (getSpeedX() > 0)
		{
			playBump();
			velocity.bounceX();
			velocity.setX(velocity.getX() - BUMP_FORCE);
			timeSinceLastSound = 0;
		}
	}

	public void collideUp()
	{
		if (getSpeedY() < 0)
		{
			playBump();
			velocity.bounceY();
			velocity.setY(velocity.getY() + BUMP_FORCE);
			timeSinceLastSound = 0;
		}
	}
	
	private void playBump()
	{
		GameEngine.gameEngine.playBump(velocity.getMagnitude());
	}
} // end class LocalPlayer
