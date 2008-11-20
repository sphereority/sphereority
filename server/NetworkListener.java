package server;

import common.*;
import common.messages.LoginMessage;

import java.lang.reflect.Array;
import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;

class NetworkListener extends Thread {
    ServerGameEngine            gameengine;
    private int                 remoteport;

    // socket to listen on and address for it
    private ServerSocketChannel channel;
    private InetSocketAddress	inetsockadd;

    // to store connection thread objects
    private int			numConnections = 5;
    private Connection	[]	connections = new Connection[numConnections];

    // for login
    UserNames			usernames;
    ObjectInputStream		istream;
    Object			obj;
    int				numBytes;
    byte		[]	bytes;
    String			message;

    NetworkListener (int rport,ServerGameEngine sge) {
        remoteport = rport;
        gameengine = sge;
    }
    public void run() {
	// a class for storing and checking user ids and passwords
	usernames = new UserNames();

        try {
    	    channel = ServerSocketChannel.open();
    	    inetsockadd = new InetSocketAddress("localhost",remoteport);
    	    channel.socket().bind(inetsockadd);
    
    	    int next = 0;
    	    while (next <= numConnections){
    	        System.out.println("NetworkListener: Listening on port " + Integer.toString(remoteport));
    	        SocketChannel connchannel = channel.accept();
    	        if (login(connchannel,next)) {
    	            ++next;
    	        }
    	    }
    	    channel.socket().close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    private boolean login(SocketChannel connchannel, int next){
	boolean log = false;
	try {
	    istream = new ObjectInputStream(connchannel.socket().getInputStream());
	    obj = istream.readObject();
	    numBytes = Array.getLength(obj);
	    bytes = new byte[numBytes];
	    for (int i=0; i<numBytes; i++)
		bytes[i] = Array.getByte(obj,i);

	    // get user name and password
            String uname;
            String upass;
	    if (LoginMessage.isLoginMessage(bytes)){
		uname = LoginMessage.getUserName(bytes);
		upass = LoginMessage.getUserPass(bytes);

		/* DEBUG
		System.out.println("User Name: " + uname);
		System.out.println("Password: " + upass);
		System.out.println(usernames.addUser(uname,upass));
		*/
		if (usernames.addUser(uname,upass)){
		    log = true;
            System.out.println("about to start connection");
	            connections[next] = new Connection(uname,gameengine,connchannel,istream);
	            connections[next].start();
		}
	    }
	}
	catch (Exception e){
	    System.out.println("Networklistener.java: DAMN! ");
	    e.printStackTrace();
	}
	return log;
    }
    public static void main (String [] args) throws Exception {
	//NetworkListener listener = new NetworkListener();
    }
}
