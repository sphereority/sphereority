package client;

import common.*;
import java.awt.geom.Rectangle2D;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class describes a human player who has local access to the keyboard and
 * mouse
 * 
 */
public class LocalPlayer extends Player {
	public static Logger logger = Logger.getLogger(CLIENT_LOGGER_NAME);

	protected InputListener inputDevice;
	protected Rectangle2D bounds = null;
	protected float timeSinceLastShot;
	protected Actor aimingTarget;
	
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
		
		System.out.printf("New player with ID %d\n", playerID);
	}
	
	public LocalPlayer(InputListener input, byte playerID, String name)
	{
		super(playerID, name);
		inputDevice = input;
		width = height = PLAYER_SIZE;
		
		System.out.printf("New player with ID %d\n", playerID);
	}
	
	public void fire()
	{
		if (timeSinceLastShot < RELOAD_TIME)
			return;
		
		super.fire();
		timeSinceLastShot = 0;
		GameEngine.gameEngine.playFire(1.0f);
		
		Projectile p = new Projectile(new Position(position), new Position(aim), curTime, curTime, (byte)playerID, team);
		GameEngine.gameEngine.addActor(p);
	}
	
	public boolean animate(float dTime, float currentTime)
	{
		timeSinceLastShot += dTime;
		
		if (inputDevice != null)
		{
			if (inputDevice.isLeftKeyPressed())  accelerate(-PLAYER_ACCELERATION*dTime, 0);
			if (inputDevice.isRightKeyPressed()) accelerate(PLAYER_ACCELERATION*dTime, 0);
			if (inputDevice.isUpKeyPressed())    accelerate(0, -PLAYER_ACCELERATION*dTime);
			if (inputDevice.isDownKeyPressed())  accelerate(0, PLAYER_ACCELERATION*dTime);
			if (inputDevice.isButtonFiring())    fire();
		}
		
		if (aimingTarget != null)
			aimAt(aimingTarget);
		
		boolean result = super.animate(dTime, currentTime);
		if (result && bounds != null)
		{
			bounds.setRect(position.getX() - 0.5f*width, position.getY() - 0.5f*height, width, height); 
		}
		return result;
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

	public Actor getAimingTarget()
	{
		return aimingTarget;
	}

	public void setAimingTarget(Actor aimingTarget)
	{
		this.aimingTarget = aimingTarget;
	}
} // end class LocalPlayer
