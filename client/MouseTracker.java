package client;

import common.*;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MouseTracker extends Actor {
	public static Logger logger = Logger.getLogger(CLIENT_LOGGER_NAME);

	protected InputListener inputListener;
	protected ClientViewArea clientViewArea;
	
	public MouseTracker(InputListener inputListener, ClientViewArea clientViewArea)
	{
		this.inputListener = inputListener;
		this.clientViewArea = clientViewArea;
		
		width = height = 0;
		position = null;
	}

	public boolean animate(float dTime, float currentTime)
	{
		// We don't change the velocity as we don't use it
		Point offset = clientViewArea.getLastOffset();
		float scale = clientViewArea.getScale();
		
		if (position == null)
			position = new Position();
		
		position.setX(((float)inputListener.getMousePosX() - offset.x)/scale);
		position.setY(((float)inputListener.getMousePosY() - offset.y)/scale);
		
		return false;
	}

	public void collision(Actor a)
	{
		// We don't collide with anything else
	}

	public void draw(Graphics2D g, float scale)
	{
		// We don't have a graphical representation
	}
}
