package	common;
/**
 * This class describes a stone which will serve as a building block for a wall
 * @author smaboshe
 *
 */

import client.gui.GuiUtils;
import java.awt.Graphics2D;
import java.awt.Graphics;

public class Stone extends Actor {
	// CONSTRUCTOR
	public Stone() {
		super();
	}

	public Stone(Position position) {
		super(position, STONE_HEIGHT, STONE_WIDTH);
	}

	// OPERATIONS
	public void draw(Graphics2D g, float scale)
	{
		g.setColor(STONE_COLOR);
		g.fillRect((int) this.position.getX(), (int) this.position.getY(), (int) this.width, (int) this.height);
	}
	
	public boolean animate(float scale) {	return true; }
}
