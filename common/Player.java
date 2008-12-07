package	common;

import client.gui.GuiUtils;
import common.messages.PlayerMotionMessage;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
//import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This (presently) represents a player
 * @author dvanhumb
 *
 */
public abstract class Player extends WeightedPosition {
	public static Logger logger = Logger.getLogger(CLIENT_LOGGER_NAME);

	protected float timeSinceLastSound;
	protected int playerID;
	protected String name;
	protected Position aim = new Position(0, 1);
	protected float curTime;
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
    
    public void setPlayerID(byte playerID) {
        this.playerID = playerID;
    }
    
    public void setPlayerName(String name) {
        this.name = name;
    }
    
    public float getCurrentTime() {
        return curTime;
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
	
    public String getPlayerName() {
        return name;
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
		
		if (aim != null)
		{
			Stroke oldStroke = g.getStroke();
			g.setStroke(new BasicStroke(3));
			g.drawLine(Math.round(position.getX() * scale),
					   Math.round(position.getY() * scale),
					   Math.round((position.getX() + aim.getX()) * scale),
					   Math.round((position.getY() + aim.getY()) * scale));
			g.setStroke(oldStroke);
		}
		
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
	
	public boolean animate(float dTime, float currentTime)
	{
		timeSinceLastSound += dTime;
		curTime = currentTime;
		
		boolean result = super.animate(dTime, currentTime);
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
	
	public void aimAt(Actor a)
	{
		aimAt(a.getPosition());
	}
	
	public void aimAt(Position p)
	{
		if (p == null)
			return;
		aim.x = p.x - position.x;
		aim.y = p.y - position.y;
		aim.scale(0.4f / aim.getMagnitude());
	}
}
