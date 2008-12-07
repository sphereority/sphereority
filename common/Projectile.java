package	common;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class describes projectile in the game
 */
public class Projectile extends Actor {
	public static Logger logger = Logger.getLogger(CLIENT_LOGGER_NAME);

	protected Position startPos;
	protected Position direction;
	protected byte owner;
	protected boolean isDelivered;
    
	public Projectile(Position startPos, Position direction, float startTime, float curTime, byte owner, int team)
	{
		this.startPos = new Position(startPos);	// Duplicate this one so we're not following somebody else
		this.direction = direction;
		this.team = team;
		
		position = new Position(startPos);
		position.move(direction, (curTime - startTime) * BULLET_SPEED);
		this.owner = owner;
        isDelivered = false;
	}
	
    /**
     * Check to see if this projectile has been delivered as a packet.
     */
    public boolean isDelivered() {
        return isDelivered;
    }
    
    /**
     * Make a note that this projectile has been delivered.
     */ 
    public void delivered() {
        isDelivered = true;
    }
    
    public Position getStartPosition() {
        return startPos;
    }
    
    public Position getDirection() {
        return direction;
    }
    
	public boolean animate(float dTime, float currentTime)
	{
		position.move(direction, dTime * BULLET_SPEED);
		
		return false;
	}

	public void collision(Actor a)
	{
		// If we bump into anything, we vanish
		if (a instanceof Player)
		{
			if ((team == TEAM_A || team == TEAM_B) && team == a.getTeam() || ((Player)a).getPlayerID() == owner)
				return;
		}
		kill();
	}

	public void draw(Graphics2D g, float scale)
	{
		if (!isAlive())
			return;
		
		if (team == TEAM_A)
			g.setColor(TEAM_A_COLOR);
		else if (team == TEAM_B)
			g.setColor(TEAM_B_COLOR);
		else
			g.setColor(TEAMLESS_COLOR);
		
		Stroke oldStroke = g.getStroke();
		g.setStroke(new BasicStroke(3));
		g.drawLine(Math.round(getX()*scale),
				   Math.round(getY()*scale),
				   Math.round((getX() + getSpeedX())*scale),
				   Math.round((getY() + getSpeedY())*scale));
		g.setStroke(oldStroke);
	}

	public byte getOwner()
	{
		return owner;
	}
	
}
