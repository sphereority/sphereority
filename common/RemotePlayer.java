package	common;

import common.messages.PlayerMotionMessage;
import java.util.Vector;

/**
 * This class describes a player who is playing the game via the network
 * @author smaboshe
 *
 */
public class RemotePlayer extends Player
{
	/**
	 * The most messages that we want to use at any one time
	 */
	public static final int MAX_SAVED_MESSAGES = 20;
	/**
	 * The oldest message we want to consider, in seconds
	 */
	public static final float OLDEST_SAVED_MESSAGE = 10;
	
	protected float currentTime;
	protected Vector<PlayerMotionMessage> messageList;
	
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
		if ((currentTime - msg.getTime()) > OLDEST_SAVED_MESSAGE)
			return;
		
		messageList.add(msg);
	}
	
	public boolean animate(float dTime, float currentTime)
	{
		this.currentTime = currentTime;
		
		float x, y, timeDiff, totalDiff;
		x = y = totalDiff = 0;
		
		for (PlayerMotionMessage m : messageList)
		{
			if ((currentTime - m.getTime()) > OLDEST_SAVED_MESSAGE)
			{
				messageList.remove(m);
				continue;
			}
			else
			{
				timeDiff = OLDEST_SAVED_MESSAGE - (currentTime - m.getTime());
				x += timeDiff * m.getVelocity().getX() + m.getPosition().getX();
				y += timeDiff * m.getVelocity().getY() + m.getPosition().getY();
				totalDiff += timeDiff;
			}
		}
		
		if (totalDiff < 0.001f)
		{
			// TODO: Add hook for missing packets here
			return false;
		}
		
		x /= totalDiff;
		y /= totalDiff;
		
		setPosition(x, y);
		
		return true;
	}
}
