package patrick;

import common.*;
import common.LoginMessage;

import java.lang.reflect.Array;
import java.net.*;
import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.*;

class CliTest {
    public static final int PORT = 44000;
    public static void main (String [] args){
	try {
	    SocketChannel channel = SocketChannel.open();
	    channel.connect(new InetSocketAddress("localhost",PORT));
	    ObjectOutputStream ostream = new ObjectOutputStream(channel.socket().getOutputStream());

	    byte [] bytes = LoginMessage.getLoginMessage("user1","password1");
	    System.out.println(LoginMessage.getMessageString(bytes));

	    ostream.writeObject(bytes);

	    ObjectInputStream istream = new ObjectInputStream(channel.socket().getInputStream());
	    Object obj = istream.readObject();
	    int numBytes = Array.getLength(obj);
	    byte [] inputbytes = new byte[numBytes];
	    for (int i=0; i< numBytes; i++)
		inputbytes[i] = Array.getByte(obj,i);
	    if (LoginMessage.isLoginSuccessMessage(inputbytes)){
	        int port = LoginMessage.getPort(inputbytes);
	        System.out.printf("Port Number: %d\n", port);
		DatagramChannel dchannel = DatagramChannel.open();
		dchannel.socket().connect(new InetSocketAddress(channel.socket().getInetAddress(),port));
		int localport = dchannel.socket().getLocalPort();
		System.out.printf("UDP local port: %d\n", localport);
		// send success message to send port number to server
		bytes = LoginMessage.getLoginSuccessMessage(localport);
		ostream.writeObject(bytes);

		
	    }
	    else{
		System.out.println("Message was not LOGIN Success");
	    }

	    channel.close();
	}
	catch (Exception e){
	    e.printStackTrace();
	}
    }
}
