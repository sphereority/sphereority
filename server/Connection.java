package server;

import common.*;
import common.LoginMessage;

import java.lang.reflect.Array;
import java.net.*;
import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.*;

class Connection extends Thread {
    /*
     * login was handled by NetworkListener.java
     * since this thread was created, NetworkListener.java:
     *   1. accepted a TCP connection from a client
     *   2. read a login message from the TCP port
     *      (obviously not the accept port)
     *   3. found a valid userid password pair in the login message
     *   4. created this thread
     *   5. called Connection.run(socket)
     *      where socket = the socket over which login happened
     * NetworkListener.java only read the login messages,
     * it sent no responses to the client
     * hence, Connection.java must acknowledge login
     */

    // this will be read in from a file later
    // the filename will be stored in constants (or in a settings file somewhere)
    public static String [] TEST_UNAMES = {"user1","user2","user3"};
    public static String [] TEST_UPASSWDS = {"firstpass","secondpass","thirdpass"};
    
    private int			bytesperchar = 8;
    private Charset		charset = Charset.forName("UTF-8");
    private Selector		selector;
    private SelectionKey	readwritesockkey;

    private Socket		sock;
    private SocketChannel	sockchannel;

    private DatagramSocket	dsock;
    private DatagramChannel	dsockchannel;

    Connection(SocketChannel sc){
	sockchannel = sc;
    }
    public void run(){
	System.out.println("Connection.run()");
	try {
	    /*
	     * Setup
	     *     first we create a udp socket
	     */
	    // create a udp socket
	    dsockchannel = DatagramChannel.open();
	    dsockchannel.socket().bind(new InetSocketAddress("localhost",0));
	    // send login success mesage + udp port number
	    int localport = dsockchannel.socket().getLocalPort();
	    byte [] bytes = LoginMessage.getLoginSuccessMessage(localport);
	    ObjectOutputStream ostream = new ObjectOutputStream(sockchannel.socket().getOutputStream());
	    ostream.writeObject(bytes);

	    // get response to success message
	    ObjectInputStream istream = new ObjectInputStream(sockchannel.socket().getInputStream());
	    Object obj = istream.readObject();
	    int numBytes = Array.getLength(obj);
	    bytes = new byte[numBytes];
	    for (int i=0; i<numBytes; i++)
		bytes[i] = Array.getByte(obj,i);
	    String message = LoginMessage.getMessageString(bytes);
	    System.out.println(message);
	    

	    //dsockchannel.connect(new InetSocketAddress("localhost",0));
	    
	    // the selector is how we poll the sockets
	    selector = Selector.open();
	    // add the tcp socket to the selector
	    sockchannel.configureBlocking(false);
	    readwritesockkey = sockchannel.register( selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE );

	    dsockchannel.configureBlocking(false);
	    //System.out.println("Remote Port: " + Integer.toString(remoteport));
	    
	    
	    /*
            byte[] buf = new byte[256];
            DatagramPacket packet;
	    String input = new String();
	    while (input.length() == 0){
	        packet = new DatagramPacket(buf, buf.length);
                dsockchannel.socket().receive(packet);
		input = new String(packet.getData());
	    }
	    System.out.println(input);
	    */
	    
	    dsockchannel.close();
	}
	catch (Exception e){
	    System.out.println("Connection.java: Damn! ");
	    e.printStackTrace();
	    System.exit(1);
	}
    }
    public static void main (String [] args) throws Exception{
	SocketChannel ch = SocketChannel.open();
	Connection c = new Connection(ch);
	c.start();
    }
}
	
