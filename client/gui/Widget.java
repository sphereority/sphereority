package client.gui;

import java.awt.*;

public abstract class Widget {	
	// SINGLETONS
	public static Logger logger = Logger.getLogger(CLIENT_LOGGER_NAME);

	protected int x, y, width, height;
	protected Color color;
	
	public Widget(int x, int y, int width, int height)
	{
		this(x, y, width, height, null);
	}
	
	public Widget(int x, int y, int width, int height, Color color)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.color = color;
	}
	
	public boolean contains(int x, int y)
	{
		if ((x < this.x) || (y < this.y))
			return false;
		return !((x > this.x+width) || (y > this.y+height));
	}
	
	public boolean containsFixed(int x, int y, int width, int height)
	{
		return getFixedBounds(width, height).contains(x, y);
	}
	
	public Rectangle getBounds()
	{
		return new Rectangle(x, y, width, height);
	}
	
	public Rectangle getFixedBounds(int width, int height)
	{
		return new Rectangle(getFixedX(width), getFixedY(height), this.width, this.height);
	}
	
	public Rectangle getFixedBounds(Dimension windowSize)
	{
		return getFixedBounds(windowSize.width, windowSize.height);
	}
	
	public abstract void draw(Graphics2D g, int windowWidth, int windowHeight);
	
	public int getX()
	{
		return x;
	}
	
	public int getFixedX(int width)
	{
		if (x < 0)
			return width + x - this.width;
		else
			return x;
	}
	
	public int getY()
	{
		return y;
	}
	
	public int getFixedY(int height)
	{
		if (y < 0)
			return height + y - this.height;
		else
			return y;
	}
	
	public int getWidth()
	{
		return width;
	}
	
	public int getHeight()
	{
		return height;
	}
	
	public Color getColor()
	{
		return color;
	}
	
	public void setColor(Color color)
	{
		this.color = color;
	}
}
