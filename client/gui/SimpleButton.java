package client.gui;

import java.awt.*;
import java.util.*;

public class SimpleButton extends InteractiveWidget
{
	protected String label;
	protected Vector<ActionCallback> callbacks;
	
	public SimpleButton(int x, int y, int width, int height, String label, Color color)
	{
		super(x, y, width, height);
		setColor(color);
		this.label = label;
		callbacks = new Vector<ActionCallback>();
	}
	
	public String getLabel()
	{
		return label;
	}
	
	public void setLabel(String label)
	{
		this.label = label;
	}
	
	public void trigger(int buttons)
	{
		System.out.printf("%s button pressed.\n", label);
		
		for (ActionCallback ac : callbacks)
			ac.actionCallback(this, buttons);
	}
	
	public void draw(Graphics2D g, int windowWidth, int windowHeight)
	{
		Color oldColor = g.getColor();
		g.setColor(color);
		
		int px = getFixedX(windowWidth), py = getFixedY(windowHeight);
		
		if (cur_state == STATE_HOVERED)
		{
			GuiUtils.drawFilledBox(g, px, py, width, height);
			g.setColor(Color.black);
		}
		else
			GuiUtils.drawOutlinedBox(g, px, py, width, height);
		
		GuiUtils.drawCenteredText(g, label, px, py, width, height, 0.5f, 0.25f, -1);
		
		g.setColor(oldColor);
	}
	
	public void addCallback(ActionCallback ac)
	{
		callbacks.add(ac);
	}
	
	public void removeCallback(ActionCallback ac)
	{
		callbacks.remove(ac);
	}
}
