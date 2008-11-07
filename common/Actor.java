package	common;

import java.awt.Graphics2D;

/**
 * This class describes an abstract class for an Actor in this game
 * @author smaboshe
 *
 */
public abstract class Actor implements Constants {
	// INSTANCE VARIABLES
	protected Position position;
	protected Position velocity;
	protected boolean alive;
	
	protected int team;
	protected int state;
	
	
	// CONSTRUCTORS
	public Actor()
	{
		position = new Position();
		velocity = new Position();
		alive = true;
		team = 0;
		state = 0;
	}
	
	
	// GETTERS
	public Position getPosition() { return position; }
	public Position getVelocity() { return velocity; }
	public int getState() { return state; }
	public int getTeam() { return team; }
	public float getSpeedX() { return velocity.x; }
	public float getSpeedY() { return velocity.y; }
	public float getX() { return position.x; }
	public float getY() { return position.y; }
	

	// SETTERS
	//protected abstract int setState();
	//protected abstract int setTeam();
	public void setState(int newState) { state = newState; }
	public void setTeam(int newTeam) { team = newTeam; }
	public void setPosition(float x, float y)
	{
		position.x = x;
		position.y = y;
	}
	
	public void moveBy(float x, float y)
	{
		position.x += x;
		position.y += y;
	}
	
	
	// OPERATIONS
	public abstract boolean animate(float dTime);
	public abstract void draw(Graphics2D g, float scale);
	public boolean isAlive() { return alive; }
}
