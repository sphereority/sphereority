package client.net;

import common.messages.*;

public class NetConnection
{
	protected int gameId;
	
	private NetConnection()
	{
		
	}
	
	public void sendMessage(Message msg)
	{
		if (msg instanceof PlayerMotionMessage)
		{
			
		}
		else
		{
			
		}
	}
	
	public static NetConnection connectToServer(String host, String password)
	{
		return null;
	}
}
