package	common;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Describes a two-dimensional postion on a coordinate plane.
 *
 */
public class Position implements Constants {
	public static Logger logger = Logger.getLogger(CLIENT_LOGGER_NAME);

	// INSTANCE VARIABLES
	protected float x;
	protected float y;
	
	
	
	// CONSTRUCTORS
	
	public Position() {
		this.x = 0.0f;
		this.y = 0.0f;
	}

	public Position(float xLocation, float yLocation) {
		this.x = xLocation;
		this.y = yLocation;
	}
	
	public Position(Position p)
	{
		x = p.x;
		y = p.y;
	}
	
	// GETTERS
	
	/* Get the X coordinate */
	public float getX() { return this.x; }

	/* Get the Y coordinate */
	public float getY() { return this.y; }
	
	/**
	 * Find the length of this vector
	 * @return	The real length of this vector
	 */
	public float getMagnitude() { return (float)Math.sqrt(x*x + y*y); }
	
	public Position subtract(Position p)
	{
		return new Position(x - p.x, y - p.y);
	}
	
	// SETTERS
	
	/* Set the X coordinate */
	public void setX(float xLocation) { this.x = xLocation; }
	
	/* Set the Y coordinate */
	public void setY(float yLocation) { this.y = yLocation; }
	
	/* Set both the X and Y coordinates for the position */
	public void setPosition(float xLocation, float yLocation) { 
		this.x = xLocation;
		this.y = yLocation;
	}
	
	// OPERATIONS
	
	/* Add to positions together */
	public Position add(Position p) {
		return new Position(p.getX() + this.x, p.getY() + this.y);
	}
	
	/* Move this position by a constant amount in both the X and Y directions */
	public void move(Position p, float amount) {
		this.x += p.x * amount;
		this.y += p.y * amount;
	}

	/* Rebound a Position against a "wall" in the X direction */
	public void bounceX() {
		this.x = this.x * -(STONE_REBOUND);
	}

	/* Rebound a Position against a "wall" in the Y direction */
	public void bounceY() {
		this.y = this.y * -(STONE_REBOUND);
	}
	
	/**
	 * Only used when this represents a velocity or acceleration vector.
	 */
	public void checkLength()
	{
		float length = getMagnitude();
		if (length > MAXIMUM_SPEED)
		{
			x = MAXIMUM_SPEED * x / length;
			y = MAXIMUM_SPEED * y / length;
		}
	}
	
	public void scale(float s)
	{
		x *= s;
		y *= s;
	}
	
	public void setDirection(Position p)
	{
		float mySize = getMagnitude();
		float pSize = p.getMagnitude();
		
		x = mySize * p.x / pSize;
		y = mySize * p.y / pSize;
	}
	
	public String toString() {
		String s = "(" + this.x + ", " + this.y + ")";
		return s;
	}
}
