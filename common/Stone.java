package	common;

//import client.gui.GuiUtils;
import java.awt.Graphics2D;

/**
 * This class describes a stone which will serve as a building block for a wall
 * @author smaboshe
 *
 */
public class Stone extends Actor {
	// CONSTRUCTOR
	public Stone() {
		super();
	}

	public Stone(Position position) {
		super(position, 1, 1);
	}

	// OPERATIONS
	public void draw(Graphics2D g, float scale)
	{
		g.setColor(STONE_COLOR);
		g.fillRect(Math.round((position.getX() - 0.5f * width) * scale),
				Math.round((position.getY() - 0.5f * height) * scale),
				Math.round(width*scale),
				Math.round(height*scale));
	}
	
	public boolean animate(float scale) {	return false; }

	public void collision(Actor a) {
		//System.out.println("A " + a.getClass().getName() + " bumped into a " + this.getClass().getName());
	}
}
