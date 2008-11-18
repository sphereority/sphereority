package	common;

import java.awt.Graphics2D;

/**
 * This class describes projectile in the game
 * @author smaboshe
 */
public class Projectile extends Actor
{
	protected Position startPos;
	protected Position direction;
	protected int owner;
	
	public Projectile(Position startPos, Position direction, float startTime, float curTime, int owner)
	{
		this.startPos = new Position(startPos);	// Duplicate this one so we're not following somebody else
		this.direction = direction;
		
		position = new Position(startPos);
		position.move(direction, (curTime - startTime) * BULLET_SPEED);
		this.owner = owner;
	}
	
	public boolean animate(float dTime)
	{
		position.move(direction, dTime * BULLET_SPEED);
		
		return false;
	}

	public void collision(Actor a)
	{
		// If we bump into anything, we vanish
		kill();
	}

	public void draw(Graphics2D g, float scale)
	{
		// TODO Auto-generated method stub
		
	}

	public int getOwner()
	{
		return owner;
	}
	
}
