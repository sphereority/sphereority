package client.gui;

import java.awt.*;

public class StringLabel extends Widget
{
	protected String label;
	protected float x_align, y_align;
	protected float font_size;
	
	StringLabel(int x, int y, int width, int height, String label)
	{
		super(x, y, width, height);
		this.label = label;
		x_align = y_align = 0.5f;
		font_size = -1;
	}
	
	StringLabel(int x, int y, int width, int height, String label, float x_align, float y_align)
	{
		super(x, y, width, height);
		this.label = label;
		this.x_align = x_align;
		this.y_align = y_align;
		checkAlignment();
		font_size = -1;
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
		
		GuiUtils.drawCenteredText(g, label, x, y, width, height, x_align, y_align, font_size);
		
		if (oldColor != null)
			g.setColor(oldColor);
	}
}
