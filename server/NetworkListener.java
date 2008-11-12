package server;

import common.*;
import common.LoginMessage;

import java.lang.reflect.Array;
import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;

class NetworkListener {
    // use DEFAULT_PORT defined in common.Constants
    public static final int PORT = Constants.DEFAULT_PORT;

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

    NetworkListener () throws Exception {
	// a class for storing and checking user ids and passwords
	usernames = new UserNames();

	channel = ServerSocketChannel.open();
	inetsockadd = new InetSocketAddress("localhost",PORT);
	channel.socket().bind(inetsockadd);

	int next = 0;
	while (next <= numConnections){
	    System.out.println("NetworkListener: Listening on port " + Integer.toString(PORT));
	    SocketChannel connchannel = channel.accept();
	    if (login(connchannel,next)) {
	        ++next;
	    }
	}
	channel.socket().close();
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
	    if (LoginMessage.isLoginMessage(bytes)){
		String uname = LoginMessage.getUserName(bytes);
		String upass = LoginMessage.getUserPass(bytes);

		/* DEBUG
		System.out.println("User Name: " + uname);
		System.out.println("Password: " + upass);
		System.out.println(usernames.checkUserPass(uname,upass));
		*/
		if (usernames.checkUserPass(uname,upass)){
		    log = true;
		}
	    }
	    connections[next] = new Connection(connchannel,istream);
	    connections[next].start();
	}
	catch (Exception e){
	    System.out.println("Networklistener.java: DAMN! ");
	    e.printStackTrace();
	}
	return log;
    }
    public static void main (String [] args) throws Exception {
	NetworkListener listener = new NetworkListener();
    }
}
