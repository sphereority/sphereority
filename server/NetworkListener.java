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
    private ServerSocket 		socket;
    private InetSocketAddress	inetsockadd;

    // to store connection thread objects
    private int					numConnections = 255;
    private Connection	[]		connections = new Connection[numConnections];

    // for login
    UserNames					usernames;
    ObjectInputStream			istream;
    Object						obj;
    int							numBytes;
    byte		[]				bytes;
    String						message;

    NetworkListener (int rport,ServerGameEngine sge) {
        remoteport = rport;
        gameengine = sge;
    }
    public void run() {
	// a class for storing and checking user ids and passwords
	usernames = new UserNames();

        try {
    	    socket = new ServerSocket(remoteport,0,InetAddress.getLocalHost());
    	    System.out.println("NetworkListener: Listening on port " + Integer.toString(remoteport));
	            	    
    	    int next = 1;
    	    while (next <= numConnections){
    	        System.out.printf("Waiting for client %d\n", next);
    	    	Socket connSocket = socket.accept();
    	        if (login(connSocket,next)) {
    	            next++;
    	        }
    	    }
    	    socket.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    private boolean login(Socket connSocket, int next){
	boolean log = false;
	try {
		BufferedInputStream istream = new BufferedInputStream(connSocket.getInputStream());
		byte [] bytes = new byte[1024];
		int numread = istream.read(bytes);
		System.out.printf("NetworkListener.java: login message, number of bytes read: %d\n", numread);
		
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
	            connections[next] = new Connection(uname,gameengine,connSocket,istream);
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
