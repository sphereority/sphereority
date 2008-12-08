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
	
	protected float currentTime;
    
    protected Position currentPosition;
    protected Position currentAim;
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
            if(messageList.peek() != null) {
                currentPosition = messageList.peek().getPosition();
                currentAim      = messageList.poll().getAim();
            }
            // Do nothing if we do not
            else
                return false;
        }
        // Check if we still have interpolating points to go through
        else if(renderQueue.peek() != null) {
            currentPosition = renderQueue.peek().getPosition();
            currentAim      = renderQueue.poll().getAim();
        }
        // Extrapolate the next point and then interpolate between the current
        // position and that point.  Add the points to the rendering queue.
        else if(messageList.peek() != null) {
            PlayerMotionMessage pm = messageList.poll();
            
            // Get 4 seperate interpolation points
            Position msgPosition = pm.getPosition();
            Position msgVelocity = pm.getVelocity();
            Position msgAim      = pm.getAim();
            
            // Extrapolation not working.
            //System.out.println(System.currentTimeMillis() - pm.getTime());
            //float extrapolatedX = msgPosition.getX() + (msgVelocity.getX() / 3f);
            //msgPosition = new Position(extrapolatedX,
            //              getLinearInterpolant(currentPosition,msgPosition,extrapolatedX));
            
            // Divisions for the positions and aims respectively
            float posDivs = (msgPosition.getX() - currentPosition.getX()) 
                                 / INTERPOLATION_SIZE;
            //float aimDivs      = (msgAim.getX() - currentAim.getX()) 
            //                     / INTERPOLATION_SIZE;

            // Avoid divide by zero!
            if(posDivs != 0) {
                // Go through all the divisions
                for(float i = 1f; i <= INTERPOLATION_SIZE; i++) {
                    // Interpolate the position
                    float positionX = currentPosition.getX() + (posDivs * i);
                    float positionY = getLinearInterpolant(msgPosition,
                                                    currentPosition,positionX);
                    
                    // Avoid division by zero
                    /*
                    if(aimDivs != 0) {
                        float aimX = currentAim.getX() + (aimDivs * i);
                        float aimY = getLinearInterpolant(msgAim,
                                                      currentAim,
                                                      aimX);
                        msgAim = new Position(aimX,aimY);
                    }*/
                    
                    // Add the result to the rendering queue
                    renderQueue.add(makeMotionMessage(new Position(positionX,
                                                                   positionY),
                                                      msgVelocity,
                                                      msgAim));
                                                     
                }
                // Use the first interpolated position/aim
                currentPosition = renderQueue.peek().getPosition();
                currentAim      = renderQueue.poll().getAim();
            }
        }
        
        /*
		if (totalWeight < 0.0001f)
		{
			// TODO: Add hook for missing packets here
			return false;
		}*/
		
		setPosition(currentPosition.getX()/totalWeight, currentPosition.getY()/totalWeight);
        aimAt(currentAim);
       
        //System.out.println(position);
		
		return super.animate(dTime,currentTime);
	}
    
    protected PlayerMotionMessage makeMotionMessage(Position pos, Position velocity,Position aim) {
        return new PlayerMotionMessage(playerID,pos,velocity,aim,(float)System.currentTimeMillis());
    }
    
    /**
     * Used to find the y value of a position given two points and the desired x.
     * @param p1 The initial position.
     * @param p2 The final position.
     * @param x The x value of the position.
     * @param y The y value of the position.
     */ 
    protected float getLinearInterpolant(Position p1, Position p2, float x) {
        return p1.getY() + ((x - p1.getX()) * ((p2.getY() - p1.getY()) / (p2.getX() - p1.getX())));
    }
}
