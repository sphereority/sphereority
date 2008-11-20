package	common;

import java.awt.Color;
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
	protected String name;
	// INSTANCE METHODS
	
	
	// CONSTRUCTORS
	public Player() {
		super();
		//timeSinceLastShot = Float.MAX_VALUE;
		timeSinceLastSound = 0;
		playerID = RANDOM.nextInt(255);
	}
	
	public Player(byte playerID, String name)
	{
		this();
		
		this.playerID = playerID;
		this.name = name;
	}
	
	// GETTERS
	public float getTimeSinceLastSound()
	{
		return timeSinceLastSound;
	}
	
	public boolean equals(Object o)
	{
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
	
	public void drawLabel(Graphics2D g, float scale)
	{
		if (name != null)
		{
			g.setColor(Color.yellow);
			g.drawString(name, Math.round(position.getX()*scale), Math.round(position.getY()*scale - scale*PLAYER_SIZE*0.6f));
		}
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
