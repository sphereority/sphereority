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
		
		for (PlayerMotionMessage m : messageList)
		{
			if ((currentTime - m.getTime()) > OLDEST_SAVED_MESSAGE)
			{
				messageList.remove(m);
				continue;
			}
		}
		
		return false;
	}
}
