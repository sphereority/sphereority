package	common;

import java.awt.Graphics2D;

import client.gui.GuiUtils;

/**
 * This (presently) represents a player
 * @author dvanhumb
 * @author smaboshe
 *
 */
public abstract class Player extends WeightedPosition {
	protected float timeSinceLastSound;
	protected int playerID;
	// INSTANCE METHODS
	
	
	// CONSTRUCTORS
	public Player() {
		super();
		//timeSinceLastShot = Float.MAX_VALUE;
		timeSinceLastSound = 0;
		playerID = RANDOM.nextInt(255);
		
		System.out.printf("Created new Player. New ID is %d\n", playerID);
	}
	
	// GETTERS
	public float getTimeSinceLastSound()
	{
		return timeSinceLastSound;
	}
	
	public boolean equals(Object o)
	{
//		if (!(o instanceof Player))
//			return false;
		
		try
		{
			return ((Player)o).playerID == this.playerID;
		}
		catch (Throwable er)
		{
			return false;
		}
	}
	
	public int getPlayerID()
	{
		return playerID;
	}
	
	
	// SETTERS
	
	
	// OPERATIONS
	public void draw(Graphics2D g, float scale) {
		if (team == TEAM_A)
			g.setColor(TEAM_A_COLOR);
		else if (team == TEAM_B)
			g.setColor(TEAM_B_COLOR);
		else
			g.setColor(TEAMLESS_COLOR);
		
		GuiUtils.drawFilledOctagon(g, Math.round(position.getX()*scale), Math.round(position.getY()*scale), scale*PLAYER_SIZE);
	}
	
	public void fire()
	{
		timeSinceLastSound = 0;
	}
	
	public boolean animate(float dTime)
	{
		timeSinceLastSound += dTime;
		
		boolean result = super.animate(dTime);
		if (! result && (timeSinceLastSound <= BLIP_TIME))
			return true;
		return result;
	}
	
	public void collision(Actor a)
	{
		// We show up again after bumping into a wall
		if (a == null || a instanceof Stone)
			timeSinceLastSound = 0;
	}
	
	public void collideLeft()
	{
		collision(null);
	}
	
	public void collideRight()
	{
		collision(null);
	}
	
	public void collideUp()
	{
		collision(null);
	}
	
	public void collideDown()
	{
		collision(null);
	}
}
