package	common;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

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

	protected int health;
	
	protected float height; // Actor height in world units
	protected int weight;
	protected float width; // Actor width in world units

	// CONSTRUCTORS
	public Actor() {
		position = new Position();
		velocity = new Position();
		alive = true;
		team = 0;
		state = 0;
		
		health = DEFAULT_ACTOR_HEALTH;
		
		height = 0;
		weight = 0;
		width = 0;
	}

	public Actor(Position initialPosition, int initialHeight, int initialWidth) {
		position = initialPosition;
		velocity = new Position();
		alive = true;
		team = 0;
		state = 0;
		
		height = initialHeight;
		weight = 0;
		width = initialWidth;
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
	
	public float getHeight() { return height; }
	public int getWeight() { return weight; }
	public float getWidth() { return width; }
	

	// SETTERS
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
	
	public void setHeight(int h) { height = h; }
	public void setWeight(int w) { weight = w; }
	public void setWidth(int w) { width = w; }

	
	// OPERATIONS
	public abstract boolean animate(float dTime);
	public abstract void draw(Graphics2D g, float scale);
	public void kill() { this.alive = false; }
	public boolean isAlive() { return (this.alive && (this.health >= MINIMUM_ACTOR_HEALTH)); }

	public Rectangle2D getBounds()
	{
		return new Rectangle2D.Float(position.getX() - 0.5f*width,
				position.getY() - 0.5f*height,
				width, height);
	}

	public abstract void collision(Actor a);
}
