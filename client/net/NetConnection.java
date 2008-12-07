package client.net;

import common.*;
import common.messages.*;
import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NetConnection implements Constants {
	public static Logger logger = Logger.getLogger(CLIENT_LOGGER_NAME);

	protected static String failReason = null;
	
	protected int gameId;
	
	// Sockets
	protected Socket tcpSocket;
	protected DatagramSocket udpSocket;
	// The address of the server's UDP socket
	protected SocketAddress serverUdp;
	// The TCP communication streams
	protected ObjectOutputStream tcpOut;
	protected ObjectInputStream tcpIn;
	
	private NetConnection()
	{
		
	}
	
	public void sendMessage(Message msg)
	{
		if (msg instanceof PlayerMotionMessage)
		{
			try
			{
				byte[] message = msg.getByteMessage();
				udpSocket.send(new DatagramPacket(message, message.length, serverUdp));
			}
			catch (IOException er)
			{
				System.out.printf("client.net.NetConnection.sendMessage(): Error sending PlayerMotionMessage to server!\n");
			}
			catch (Exception er)
			{
				System.out.printf("client.net.NetConnection.sendMessage(): Unknown error sending PlayerMotionMessage to server!\n");
			}
		}
		else
		{
			try
			{
				byte[] message = msg.getByteMessage();
				tcpOut.writeObject(message);
			}
			catch (IOException er)
			{
				System.out.printf("client.netNetConnection.sendMessage(): Error sending TCP message to server!\n");
			}
			catch (Exception er)
			{
				System.out.printf("client.net.NetConnection.sendMessage(): Unknown error sending TCP message to server!\n");
			}
		}
	} // end sendMessage()
	
	public static NetConnection connectToServer(String host, String name, String password)
	{
		try
		{
			Socket tcpSocket = new Socket(host, DEFAULT_PORT);
			if (!tcpSocket.isConnected())
			{
				System.err.printf("Failed to connect to server '%s'\n", host);
				return null;
			}
			
			ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(tcpSocket.getInputStream()));
			ObjectOutputStream out = new ObjectOutputStream(tcpSocket.getOutputStream());
			
			// Send login information
			out.writeObject(LoginMessage.getLoginMessage(name, password));
			
			// Get response
			Object response = in.readObject();
			System.out.printf("Result object is of type '%s'.\n", response.getClass().getName());
		}
		catch (ClassNotFoundException er)
		{
			return null;
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
