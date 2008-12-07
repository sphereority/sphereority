package	common;

import java.awt.geom.Rectangle2D;
import java.awt.Graphics2D;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class describes an abstract class for an Actor in this game
 *
 */
public abstract class Actor implements Constants {
	public static Logger logger = Logger.getLogger(CLIENT_LOGGER_NAME);

	// INSTANCE VARIABLES
	protected Position position;
	protected Position velocity;
	protected boolean alive;
	
	protected int team;
	protected int state;

	protected int health;
	
	protected float height; // Actor height in world units
	protected float width; // Actor width in world units

	protected float weight; // In case we want to have weight affecting our actors

	// CONSTRUCTORS
	public Actor() {
		position = new Position();
		velocity = new Position();
		alive = true;
		team = 0;
		state = 0;
		
		health = DEFAULT_ACTOR_HEALTH;		
		height = DEFAULT_ACTOR_HEIGHT;
		weight = DEFAULT_ACTOR_WEIGHT;
		width = DEFAULT_ACTOR_WIDTH;

		// Log this action
		logger.log(Level.INFO, "Created: " + this);
	}

	public Actor(Position initialPosition, int initialHeight, int initialWidth) {
		position = initialPosition;
		velocity = new Position();
		alive = true;
		team = 0;
		state = 0;

		health = DEFAULT_ACTOR_HEALTH;		
		height = initialHeight;
		weight = DEFAULT_ACTOR_WEIGHT;
		width = initialWidth;

		// Log this action
		logger.log(Level.INFO, "Created: " + this);
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
	public float getWeight() { return weight; }
	public float getWidth() { return width; }
	

	// SETTERS
	public void setState(int newState) { state = newState; }
	public void setTeam(int newTeam) { team = newTeam; }
	public void setPosition(float x, float y)
	{
		position.x = x;
		position.y = y;
	}
	public void setPosition(Position p)
	{
		position.x = p.x;
		position.y = p.y;
	}
	
	public void moveBy(float x, float y)
	{
		position.x += x;
		position.y += y;
	}
	
	public void setHeight(float h) { height = h; }
	public void setWidth(float w) { width = w; }

	public void setWeight(float w) { weight = w; }
	
	// OPERATIONS
	public abstract boolean animate(float dTime, float currentTime);
	public abstract void draw(Graphics2D g, float scale);
	public void kill() { this.alive = false; }
	public boolean isAlive()
    {
        return alive;
        //&& (this.health >= MINIMUM_ACTOR_HEALTH));
    }


	public Rectangle2D getBounds()	{
		// Since the Actors origin is in the middle of the actor we'll have to wiggle the coordinates to generate the correct bounding box
		return new Rectangle2D.Float(position.getX() - 0.5f*width, position.getY() - 0.5f*height, width, height);
	}
	
	/**
	 * When this Actor collides with another Actor, this method gets called
	 * @param a	The other Actor that we collided with, or null if it's a wall
	 */
	public abstract void collision(Actor a);
	
	public String toString() {
		String s = this.getClass().getName() + ": Position: " + position + ", Velocity: " + velocity + ", width: " + this.width + " height: " + this.height;
		return s;
	}

    public int getHealth()
    {
        return health;
    }

    public void setHealth(int health)
    {
        this.health = health;
    }
}
