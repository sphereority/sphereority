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

class CliTest {
    public static final int PORT = 44000;
    public static void main (String [] args){
	try {
	    System.out.println("CliTest.java");
	    // create TCP socket channel
	    SocketChannel channel = SocketChannel.open();
	    System.out.println("CliTest.java: channel created");
	    channel.connect(new InetSocketAddress("localhost",PORT));
	    System.out.println("CliTest.java: channel socket connected");
	    
	    // create  & send a login message
	    byte [] bytes = LoginMessage.getLoginMessage("user1","password1");
	    System.out.printf("CliTest.java: First message length: %d\n", bytes.length);
	    ByteBuffer buf = ByteBuffer.allocate(4096);
	    buf.put(bytes);
	    System.out.println("CliTest.java: login message created");
	    System.out.println(LoginMessage.getMessageString(bytes));
	    buf.flip();
	    System.out.printf("CliTest.java: buf.remaining before channel.write(): %d\n", buf.remaining()); 
	    int numwritten = channel.write(buf);
	    System.out.printf("CliTest.java: first mesage number of bytes written: %d\n", numwritten);
	    
	    // read reply message
	    buf.clear();
	    int numread = channel.read(buf);
	    System.out.printf("CliTest.java: first read: number of bytes read: %d\n", numread);
	    bytes = new byte[numread];
	    buf.flip();
	    buf.get(bytes);
	    if (LoginMessage.isLoginSuccessMessage(bytes)){
	    	// get remote port number from success message
	        int port = LoginMessage.getPort(bytes);
	        System.out.printf("Port Number: %d\n", port);
	        // create datagram channel & connect to rem port
	        DatagramChannel dchannel = DatagramChannel.open();
            dchannel.socket().connect(new InetSocketAddress(channel.socket().getInetAddress(),port));
	        // get localport of datagram socket
	        int localport = dchannel.socket().getLocalPort();
	        System.out.printf("UDP local port: %d\n", localport);
	        // send success message to send port number to server
	        bytes = LoginMessage.getLoginSuccessMessage(localport);
	        buf.clear();
	        buf.put(bytes);
	        buf.flip();
	        channel.write(buf);
	        
	        DeathMessage dm = new DeathMessage((byte) 1,(byte) 1,(byte) 1);
	        bytes = dm.getByteMessage();
	        buf.clear();
	        buf.put(bytes);
	        buf.flip();
	        channel.write(buf);	        
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
