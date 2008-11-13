package	common;

//import client.gui.GuiUtils;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

/**
 * This class describes a stone which will serve as a building block for a wall
 * @author smaboshe
 *
 */
public class Stone extends Actor {
	protected Rectangle2D bounds = null;
	
	// CONSTRUCTOR
	public Stone() {
		super();
	}
	
	public Stone(int x, int y)
	{
		this(new Position(x+0.5f, y+0.5f));
	}
	public Stone(float x, float y)
	{
		this(new Position(x, y));
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
	
	public boolean animate(float scale) { return false; }

	public void collision(Actor a) {
		//System.out.println("A " + a.getClass().getName() + " bumped into a " + this.getClass().getName());
	}
	
	public Rectangle2D getBounds()
	{
		if (bounds == null)
			bounds = super.getBounds();
		
		return bounds;
	}
}
