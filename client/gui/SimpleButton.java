package client.gui;

import common.Constants;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SimpleButton extends InteractiveWidget implements Constants {
	public static Logger logger = Logger.getLogger(CLIENT_LOGGER_NAME);

	protected Vector<ActionCallback> callbacks;
	protected Polygon outline, fill;
	
	public SimpleButton(int x, int y, int width, int height, String label, Color color)
	{
		super(x, y, width, height, label);
		setColor(color);
		callbacks = new Vector<ActionCallback>();
		
		outline = GuiUtils.getBoxShape(x, y, width, height);
		fill = GuiUtils.getBoxShape(x+2, y+2, width-3, height-3);
	}
	
	public void trigger(int buttons)
	{
		System.out.printf("%s button pressed.\n", label);
		logger.log(Level.INFO, "" + label + " button pressed");
		
		for (ActionCallback ac : callbacks)
			ac.actionCallback(this, buttons);
	}
	
	public void draw(Graphics2D g, int windowWidth, int windowHeight)
	{
		Color oldColor = g.getColor();
		g.setColor(color);
		
		int px = getFixedX(windowWidth), py = getFixedY(windowHeight);
		
		AffineTransform oldTransform = null;
		if (px != x || py != y)
		{
			oldTransform = g.getTransform();
			if (px != x)
				g.translate(windowWidth-width, 0);
			if (py != y)
				g.translate(0, windowHeight-height);
		}
		
		g.draw(outline);
		
//		g.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 127));	
//		g.fill(outline);
		
		if (cur_state == STATE_HOVERED)
		{
			g.setColor(color);
			g.fill(fill);
			g.setColor(Color.black);
		}
		
		if (oldTransform != null)
			g.setTransform(oldTransform);
		
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
