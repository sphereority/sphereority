package patrick;

import common.*;
import common.messages.*;

import java.lang.reflect.Array;
import java.net.*;
import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.*;

class CliTest
{
	public static final int PORT = 44000;

	public static void main(String[] args)
	{
		try
		{
			System.out.println("CliTest.java");
			// create TCP socket channel
			SocketChannel channel = SocketChannel.open();
			// System.out.println("CliTest.java: channel created");
			channel.connect(new InetSocketAddress("localhost", PORT));
			// System.out.println("CliTest.java: channel socket connected");
			// create objectoutputstream & objectinputstream for TCP channel
			ObjectOutputStream ostream = new ObjectOutputStream(channel
					.socket().getOutputStream());
			// System.out.println("CliTest.java: ObjectOutputStream created");

			// create & send a login message
			byte[] bytes = LoginMessage.getLoginMessage("user1", "password1");
			// System.out.println("CliTest.java: login message created");
			System.out.println(LoginMessage.getMessageString(bytes));
			ostream.writeObject(bytes);

	    // read reply message
	    ObjectInputStream istream = new ObjectInputStream(channel.socket().getInputStream());
	    System.out.println("CliTest.java: ObjectInputStream created");
	    Object obj = istream.readObject();
	    int numBytes = Array.getLength(obj);
	    byte [] inputbytes = new byte[numBytes];
	    for (int i=0; i< numBytes; i++)
		inputbytes[i] = Array.getByte(obj,i);
	    // if login was successful
	    if (LoginMessage.isLoginSuccessMessage(inputbytes)){
		// get remote port number from success message
	        int port = LoginMessage.getPort(inputbytes);
	        System.out.printf("Port Number: %d\n", port);
		// create datagram channel & connect to rem port
		DatagramChannel dchannel = DatagramChannel.open();
                //dchannel.socket().bind(new InetSocketAddress("localhost",44001));
		dchannel.socket().connect(new InetSocketAddress(channel.socket().getInetAddress(),port));
		// get localport of datagram socket
		int localport = dchannel.socket().getLocalPort();
		System.out.printf("UDP local port: %d\n", localport);
		// send success message to send port number to server
		bytes = LoginMessage.getLoginSuccessMessage(localport);
		ostream.writeObject(bytes);
        //ostream.writeObject((new DeathMessage( (byte) 1, (byte) 1, (byte) 1 )).getByteMessage());
//        ostream.writeObject((new DeathMessage( (byte) 2, (byte) 2, (byte) 2 )).getByteMessage());
//        ostream.writeObject((new DeathMessage( (byte) 3, (byte) 3, (byte) 3 )).getByteMessage());
//        ostream.writeObject((new DeathMessage( (byte) 4, (byte) 4, (byte) 4 )).getByteMessage());
	    }
	    else{
		System.out.println("Message was not LOGIN Success");
	    }

			// read reply message
			ObjectInputStream istream = new ObjectInputStream(channel.socket().getInputStream());
			System.out.println("CliTest.java: ObjectInputStream created");
			
			Object obj = istream.readObject();
			int numBytes = Array.getLength(obj);
			byte[] inputbytes = new byte[numBytes];
			for (int i = 0; i < numBytes; i++)
				inputbytes[i] = Array.getByte(obj, i);
			
			// if login was successful
			if (LoginMessage.isLoginSuccessMessage(inputbytes))
			{
				// get remote port number from success message
				int port = LoginMessage.getPort(inputbytes);
				System.out.printf("Port Number: %d\n", port);
				// create datagram channel & connect to rem port
				DatagramChannel dchannel = DatagramChannel.open();
				// dchannel.socket().bind(new
				// InetSocketAddress("localhost",44001));
				dchannel.socket().connect(new InetSocketAddress(channel.socket().getInetAddress(), port));
				// get localport of datagram socket
				int localport = dchannel.socket().getLocalPort();
				System.out.printf("UDP local port: %d\n", localport);
				// send success message to send port number to server
				bytes = LoginMessage.getLoginSuccessMessage(localport);
				ostream.writeObject(bytes);
				for (int i = 0; i < bytes.length; i++)
				{
					System.out.println(bytes[i]);
				}
			}
			else
			{
				System.out.println("Message was not LOGIN Success");
			}

			channel.close();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
