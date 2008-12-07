package	common;

import common.messages.PlayerMotionMessage;
import java.util.concurrent.*;
import java.util.ConcurrentModificationException;
//import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Vector;

/**
 * This class describes a player who is playing the game via the network
 *
 */
public class RemotePlayer extends Player {
	public static Logger logger = Logger.getLogger(CLIENT_LOGGER_NAME);

	/**
	 * The most messages that we want to use at any one time
	 */
	public static final int MAX_SAVED_MESSAGES = 20;
	/**
	 * The oldest message we want to consider, in seconds
	 */
	public static final float OLDEST_SAVED_MESSAGE = 5;
    
    public static final boolean JUST_USE_LAST_MESSAGE = true;
	
	protected float currentTime;
	protected Vector<PlayerMotionMessage> messageList;
	
	private Semaphore lock = new Semaphore(1);
	
	/**
	 * Creates a RemotePlayer with the specified id and name
	 * @param playerID	The id of this player
	 * @param name		The text name of this player
	 */
	public RemotePlayer(byte playerID, String name)
	{
		super(playerID, name);
		
		messageList = new Vector<PlayerMotionMessage>();
		currentTime = -1;
	}
	
	public void addMotionPacket(PlayerMotionMessage msg)
	{
		if ((currentTime - msg.getTime()) > OLDEST_SAVED_MESSAGE && currentTime > 0)
			return;
		
		try
		{
			lock.acquire();
			messageList.add(msg);
			
			if (messageList.size() > MAX_SAVED_MESSAGES)
				messageList.removeElementAt(0);
			
			lock.release();
		}
		catch (InterruptedException er)
		{
			
		}
	}
	
	public boolean animate(float dTime, float currentTime)
	{
		this.currentTime = currentTime;
		
		float x, y, timeD, totalWeight, posX, posY, weight;
		x = y = totalWeight = 0;
		// float weight;
		
		do
		{
			try
			{
				//lock.acquire();
//				for (PlayerMotionMessage m : messageList)
//					if ((currentTime - m.getTime()) > OLDEST_SAVED_MESSAGE)
//						messageList.remove(m);
				
				if (messageList.size() < 1)
					return false;
				
                if (JUST_USE_LAST_MESSAGE)
                {
    				x = messageList.get(messageList.size()-1).getPosition().getX();
    				y = messageList.get(messageList.size()-1).getPosition().getY();
    				totalWeight = 1;
                }
                else
                {
    				for (PlayerMotionMessage m : messageList)
    				{
    				    timeD = currentTime - m.getTime();
                        if (timeD < 0)
                        {
    //                        System.out.print("&");
                            continue;
                        }
    					posX = timeD * m.getVelocity().getX() + m.getPosition().getX();
    					posY = timeD * m.getVelocity().getY() + m.getPosition().getY();
                        weight = MAX_SAVED_MESSAGES - timeD;
                        x += weight * posX;
                        y += weight * posY;
    					totalWeight += weight;
    				}
                }
				//lock.release();
			}
//			catch (InterruptedException er)
//			{
//				continue;
//			}
			catch (ConcurrentModificationException er)
			{
				//continue;
				return false;
			}
		} while (false);
		
		if (totalWeight < 0.0001f)
		{
			// TODO: Add hook for missing packets here
			return false;
		}
		
		setPosition(x / totalWeight, y / totalWeight);
        System.out.println(position);
		
		return true;
	}
}
