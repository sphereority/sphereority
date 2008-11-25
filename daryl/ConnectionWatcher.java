package daryl;

import java.io.*;
import java.net.*;

import common.*;
import common.messages.*;

public class ConnectionWatcher extends Thread implements Constants
{
	private MulticastSocket multicastSocket;
	
	public ConnectionWatcher(int port) throws IOException
	{
		multicastSocket = new MulticastSocket(port);
		multicastSocket.connect(InetAddress.getByName(MCAST_ADDRESS), port);
		
		System.out.printf("Successfully connected to %s on port %d.\n", MCAST_ADDRESS, port);
		System.out.println("Listening to messages...");
	}
	
	public void run()
	{
		while (true)
		{
			try
			{
				DatagramPacket packet = new DatagramPacket(new byte[1024], 1024);
				multicastSocket.receive(packet);
				System.out.printf("Recieved packet of length %d\n", packet.getLength());
				
				Message message = MessageAnalyzer.getMessage(packet.getData());
				if (!(message instanceof PlayerMotionMessage))
					continue;
				
				PlayerMotionMessage pmm = (PlayerMotionMessage)message;
				System.out.printf("Player %d moved to (%.2f, %.2f)\n", pmm.getPlayerId(), pmm.getPosition().getX(), pmm.getPosition().getY());
			}
			catch (IOException er)
			{
				
			}
			catch (Exception er)
			{
				
			}
		}
	}
	
	public static void main(String[] args)
	{
		try
		{
			ConnectionWatcher watcher = new ConnectionWatcher(MCAST_PORT);
			watcher.start();
		}
		catch (IOException er)
		{
			System.out.println("Failed to start connection watcher.");
		}
	}
}
