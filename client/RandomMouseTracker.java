package client;

import common.*;
//import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Random;
//import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is meant to be used with bots!
 */
public class RandomMouseTracker extends MouseTracker {
	public static Logger logger = Logger.getLogger(CLIENT_LOGGER_NAME);

	protected Random random;
    protected int posX, posY;
    
	public RandomMouseTracker(InputListener inputListener, ClientViewArea clientViewArea)
	{
        super(inputListener,clientViewArea);
        this.random = new Random();
		
		posX = posY = 0;
		position = null;
    }

	public boolean animate(float dTime, float currentTime)
	{
		// We don't change the velocity as we don't use it
		Point offset = clientViewArea.getLastOffset();
		float scale = clientViewArea.getScale();
		
		if (position == null)
			position = new Position();
		
        // Reset if it went too far
        if(posX > GAME_WINDOW_WIDTH)
            posX = 0;
        if(posY > GAME_WINDOW_HEIGHT)
            posY = 0;
            
        position.setX(((float)posX - offset.x)/scale);
		position.setY(((float)posY - offset.y)/scale);
		
        // Move the aim around gradually
        posX += random.nextInt(10);
        posY += random.nextInt(10);
		return false;
	}
}
