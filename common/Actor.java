package	common;

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
	
	
	// GETTERS
	protected Position getPosition() { return this.position; }
	protected Position getVelocity() { return this.velocity; }
	protected int getState() { return this.state; }
	protected int getTeam() { return this.team; }
	

	// SETTERS
	protected abstract int setState();
	protected abstract int setTeam();
	protected void setState(int newState) { this.state = newState; }
	protected void setTeam(int newTeam) { this.team = newTeam; }
	
	
	// OPERATIONS
	protected abstract void animate();
	protected abstract void draw();
	protected boolean isAlive() { return this.alive == true; }
}
