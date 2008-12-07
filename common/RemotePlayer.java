package	common;

import common.messages.PlayerMotionMessage;
import java.util.concurrent.*;
import java.util.ConcurrentModificationException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.LinkedList;
import java.util.Queue;

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
    
    public static final boolean JUST_USE_LAST_MESSAGE = false;
	
    public final float DIV_SIZE = 15f;
    
	protected float currentTime;
    
    protected Position currentPosition;
	protected Queue<PlayerMotionMessage> messageList;
	protected Queue<PlayerMotionMessage> renderQueue;
    
	private Semaphore lock = new Semaphore(1);
	
	/**
	 * Creates a RemotePlayer with the specified id and name
	 * @param playerID	The id of this player
	 * @param name		The text name of this player
	 */
	public RemotePlayer(byte playerID, String name)
	{
		super(playerID, name);
		
		messageList = new LinkedList<PlayerMotionMessage>();
		renderQueue = new LinkedList<PlayerMotionMessage>();
        currentTime = -1;
	}
	
	public void addMotionPacket(PlayerMotionMessage msg)
	{
        messageList.add(msg);
	}
	
	public boolean animate(float dTime, float currentTime)
	{
		// float weight;
		
        /*
        lock.acquire();
//				for (PlayerMotionMessage m : messageList)
//					if ((currentTime - m.getTime()) > OLDEST_SAVED_MESSAGE)
//						messageList.remove(m);
        
        // Don't process if we do not have at least on message
        if (messageList.size() < 2)
            return false;
        
        if (JUST_USE_LAST_MESSAGE)
        {
            int index = messageList.size() - 1;
            PlayerMotionMessage mostRecent = messageList.get(index);
            /*
            for (PlayerMotionMessage m : messageList)
            {
                if (mostRecent.getTime() < m.getTime())
                    mostRecent = m;
            }
            timeD = curTime - mostRecent.getTime();
            x = mostRecent.getPosition().getX();
            y = mostRecent.getPosition().getY();
            totalWeight = 1;
        }
        else
        {
            for (PlayerMotionMessage m : messageList)
            {
            
            }
        }
        lock.release();*/
                
        /*        
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
		} while (false);*/
		
        this.currentTime = currentTime;
		
		float timeD, totalWeight;
		totalWeight = 1;
        
        // Do we have a starting position for this remote player yet?
        if(currentPosition == null) {
            // Do we have an initial message?
            if(messageList.peek() != null)
                currentPosition = messageList.poll().getPosition();
            // Do nothing if we do not
            return false;
        }
        // Check if we still have interpolating points to go through
        else if(renderQueue.peek() != null) {
            currentPosition = renderQueue.poll().getPosition();
        }
        // Extrapolate the next point and then interpolate between the current
        // position and that point.  Add the points to the rendering queue.
        else if(messageList.peek() != null) {
            PlayerMotionMessage pm = messageList.poll();
            
            // Get 4 seperate interpolation points
            Position msgPosition = pm.getPosition();
            Position msgVelocity = pm.getVelocity();
            
            // Extrapolation not working.
            //System.out.println(System.currentTimeMillis() - pm.getTime());
            //float extrapolatedX = msgPosition.getX() + (msgVelocity.getX() / 3f);
            //msgPosition = new Position(extrapolatedX,
            //              getLinearInterpolant(currentPosition,msgPosition,extrapolatedX));
            
            float divs = (msgPosition.getX() - currentPosition.getX()) / DIV_SIZE;
            
            // Avoid divide by zero!
            if(divs != 0) {
                for(float i = 0f; i < DIV_SIZE; i++) {
                    float thisDiv = currentPosition.getX() + (divs * i);
                    renderQueue.add(new PlayerMotionMessage((byte)playerID,
                                        new Position(thisDiv,getLinearInterpolant(msgPosition,currentPosition,thisDiv)),
                                        msgVelocity,
                                        (float)System.currentTimeMillis()));
                                                     
                }
                currentPosition = renderQueue.poll().getPosition();
            }
        }
        
        /*
		if (totalWeight < 0.0001f)
		{
			// TODO: Add hook for missing packets here
			return false;
		}*/
		
		setPosition(currentPosition.getX()/totalWeight, currentPosition.getY()/totalWeight);
        //System.out.println(position);
		
		return true;
	}
    
    protected float getLinearInterpolant(Position p1, Position p2, float x) {
        return p1.getY() + ((x - p1.getX()) * ((p2.getY() - p1.getY()) / (p2.getX() - p1.getX())));
    }
}
