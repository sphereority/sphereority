package	sphereority;


/**
 * Describes a two-dimensional postion on a coordinate plane.
 * @author smaboshe
 *
 */
public class Position implements Constants {
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
	
	// GETTERS
	
	/* Get the X coordinate */
	public float getX() { return this.x; }

	/* Get the Y coordinate */
	public float getY() { return this.y; }
	
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
		this.x = this.x * amount;
		this.y = this.y * amount;
	}

	/* Rebound a Position against a "wall" in the X direction */
	public void bounceX() {
		this.x = this.x * -(WALL_REBOUND);
	}

	/* Rebound a Position against a "wall" in the Y direction */
	public void bounceY() {
		this.y = this.y * -(WALL_REBOUND);
	}
}
