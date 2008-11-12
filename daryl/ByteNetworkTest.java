package daryl;

import java.net.*;
import java.io.*;

public class ByteNetworkTest
{	
	public static void main(String[] args)
	{
		try
		{
			Socket socket = new Socket("localhost", 5000);
			
			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
			
			out.write(0x50);
			out.writeObject("This is a random string.");
			
			out.close();
			
			SocketAddress address = socket.getRemoteSocketAddress();
			if (address instanceof InetSocketAddress)
			{
				InetSocketAddress addr = (InetSocketAddress)address;
				System.out.printf("Server at %s:%d\n", addr.getAddress().getHostAddress(), addr.getPort());
			}
			else
				System.out.println("Unknown socket type.");
			socket.close();
			
			System.out.println("Data sent.");
		}
		catch (IOException er)
		{
			er.printStackTrace();
		}
	}
} // end class ByteNetworkTest
