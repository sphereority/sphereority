package daryl;

import java.io.*;
import java.nio.channels.*;
import java.net.*;

public class DatagramTest
{
	public static void main(String[] args)
	{
		try
		{
			// Actual useful code:
			DatagramChannel channel = DatagramChannel.open();
			channel.configureBlocking(false);
			channel.connect(new InetSocketAddress("localhost", 0));
			DatagramSocket dataSocket = channel.socket();
			// End actual useful code
			
			if (!dataSocket.isBound())
				System.out.println("Socket not connected.");
			System.out.printf("Port number: %d\n", dataSocket.getLocalPort());
			System.out.printf("Address is: %s\n", ((InetSocketAddress)dataSocket.getLocalSocketAddress()).getAddress().getHostAddress());
			if (dataSocket.getChannel() == null)
				System.out.println("No DatagramChannel associated with this DatagramSocket!");
			else
				System.out.println("This DatagramSocket has a DatagramChannel.");
			if (channel.isBlocking())
				System.out.println("Blocking enabled.");
			else
				System.out.println("Blocking disabled.");
			System.out.printf("The DatagramChannel is an instance of %s\n", channel.getClass().getName());
			
			dataSocket.close();
		}
		catch (IOException er)
		{
			er.printStackTrace();
		}
	}
}
