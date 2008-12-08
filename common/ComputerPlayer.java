package	common;

import client.*;
import java.awt.geom.Rectangle2D;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class describes an artificial game player (a "bot")
 *
 */
public class ComputerPlayer extends LocalPlayer {
	public static Logger logger = Logger.getLogger(CLIENT_LOGGER_NAME);

	// Key event-related variables
	private boolean[] keysPressed;
	protected java.util.Random random;
    
    protected int direction;
    protected long lastTime;
    protected GameEngine engine;
	protected Rectangle2D bounds = null;
	protected float timeSinceLastShot;
	protected Actor aimingTarget;
	
	public ComputerPlayer(byte playerID, String name,GameEngine engine)
	{
		super(null,playerID, name);
		width = height = PLAYER_SIZE;
        random = new java.util.Random();
        lastTime = System.currentTimeMillis();
        direction = random.nextInt(101);
        this.engine = engine;
		System.out.printf("New player with ID %d\n", playerID);
	}
	
	public void fire()
	{
		if (timeSinceLastShot < RELOAD_TIME)
			return;
		
		super.fire();
		timeSinceLastShot = 0;
		GameEngine.gameEngine.playFire(1.0f);
		
		Projectile p = new Projectile(new Position(position), new Position(aim), curTime, curTime, (byte)playerID, team);
		GameEngine.gameEngine.addActor(p);
	}
	
	public boolean animate(float dTime, float currentTime)
	{
		timeSinceLastShot += dTime;
		
        // Go left
        if (direction < 25)  accelerate(-PLAYER_ACCELERATION*dTime, 0);
        // Go right
        if (direction > 45 && direction < 70) accelerate(PLAYER_ACCELERATION*dTime, 0);
        // Go down
        if (direction > 65)    accelerate(0, -PLAYER_ACCELERATION*dTime);
        // Go up
        if (direction > 20 && direction < 50)  accelerate(0, PLAYER_ACCELERATION*dTime);
        // Fire
        if (random.nextInt(101) % 10 == 0)    fire();
		
        // Change directions every 5 seconds
        if(System.currentTimeMillis() - lastTime > 1000) {
            direction = random.nextInt(101);
            lastTime = System.currentTimeMillis();
        }
        // Reverse direction if there is a collision
        else if(collisionX || collisionY) {
            direction = ( (direction + 40) % 100);
            lastTime = System.currentTimeMillis();
            collisionX = collisionY = false;
        }
        
        boolean result = super.animate(dTime, currentTime);
		if (result && bounds != null)
		{
			bounds.setRect(position.getX() - 0.5f*width, position.getY() - 0.5f*height, width, height); 
		}
		return result;
	}
    
    private void updateMousePosition(java.awt.event.MouseEvent e) {
	}
}