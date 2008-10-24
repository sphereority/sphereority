package client.gui;

import java.awt.*;
import java.util.*;

public class SimpleButton extends InteractiveWidget
{
	protected Vector<ActionCallback> callbacks;
	
	public SimpleButton(int x, int y, int width, int height, String label, Color color)
	{
		super(x, y, width, height, label);
		setColor(color);
		callbacks = new Vector<ActionCallback>();
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
		
		GuiUtils.drawOutlinedBox(g, px, py, width, height);
		
		if (cur_state == STATE_HOVERED)
		{
			GuiUtils.drawFilledBox(g, px+2, py+2, width-3, height-3);
			g.setColor(Color.black);
		}
		
		GuiUtils.drawCenteredText(g, label, px, py, width, height, 0.5f, 0.25f, fontSize);
		
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
