package client.net;

import common.*;
import common.messages.*;

import java.net.*;
import java.io.*;

import javax.print.attribute.standard.Severity;

public class NetConnection implements Constants
{
	protected static String failReason = null;
	
	protected int gameId;
	protected Socket tcpSocket;
	protected DatagramSocket udpSocket; 
	
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
		try
		{
			Socket tcpSocket = new Socket(host, SERVER_GAME_ENGINE_PORT);
			if (!tcpSocket.isConnected())
			{
				System.err.printf("Failed to connect to server '%s'\n", host);
				return null;
			}
			
			ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(tcpSocket.getInputStream()));
			ObjectOutputStream out = new ObjectOutputStream(tcpSocket.getOutputStream());
			
		}
		catch (IOException er)
		{
			return null;
		}
		return null;
	}
	
	public static String getReasonFailed()
	{
		return failReason;
	}
}
