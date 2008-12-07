package client.gui;

import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StringLabel extends Widget {
	public static Logger logger = Logger.getLogger(CLIENT_LOGGER_NAME);

	protected String label;
	protected float x_align, y_align;
	protected float fontSize;
	
	public StringLabel(int x, int y, int width, int height, String label)
	{
		this(x, y, width, height, label, 0.5f, 0.5f);
	}
	
	public StringLabel(int x, int y, int width, int height, String label, Color color)
	{
		this(x, y, width, height, label, 0.5f, 0.5f, color);
	}
	
	public StringLabel(int x, int y, int width, int height, String label, float x_align, float y_align)
	{
		this(x, y, width, height, label, x_align, y_align, null);
	}
	
	public StringLabel(int x, int y, int width, int height, String label, float x_align, float y_align, Color color)
	{
		super(x, y, width, height, color);
		this.label = label;
		this.x_align = x_align;
		this.y_align = y_align;
		checkAlignment();
		fontSize = -1;
	}
	
	public void setAlignment(float x, float y)
	{
		x_align = x;
		y_align = y;
		checkAlignment();
	}
	
	public float getXAlignment()
	{
		return x_align;
	}
	
	public float getYAlignment()
	{
		return y_align;
	}
	
	public float getFontSize()
	{
		return fontSize;
	}
	
	public void setFontSize(float size)
	{
		fontSize = size;
	}
	
	protected void checkAlignment()
	{
		x_align = Math.max(Math.min(x_align, 1), 0);
		y_align = Math.max(Math.min(y_align, 1), 0);
	}
	
	public void draw(Graphics2D g, int windowWidth, int windowHeight)
	{
		Color oldColor = null;
		if (color != null)
		{
			oldColor = g.getColor();
			g.setColor(color);
		}
		
		GuiUtils.drawCenteredText(g, label, getFixedX(windowWidth), getFixedY(windowHeight), width, height, x_align, y_align, fontSize);
		
		if (oldColor != null)
			g.setColor(oldColor);
	}
}
