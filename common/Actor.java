package	common;

import java.awt.Graphics2D;
import java.awt.Rectangle;

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
	
	protected int height; // Actor height in pixels
	protected int weight;
	protected int width; // Actor width in pixels

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
	
	public int getHeight() { return height; }
	public int getWeight() { return weight; }
	public int getWidth() { return width; }
	

	// SETTERS
	public void setState(int newState) { state = newState; }
	public void setTeam(int newTeam) { team = newTeam; }
	
	public void setHeight(int h) { height = h; }
	public void setWeight(int w) { weight = w; }
	public void setWidth(int w) { width = w; }

	
	// OPERATIONS
	public abstract boolean animate(float dTime);
	public abstract void draw(Graphics2D g, float scale);
	public void kill() { this.alive = false; }
	public boolean isAlive() { return (this.alive && (this.health >= MINIMUM_ACTOR_HEALTH)); }

	public Rectangle getBounds() { return new Rectangle((int) this.position.getX(), (int) this.position.getY(), this.width, this.height); }

	public void collision(Actor a) {}
}
