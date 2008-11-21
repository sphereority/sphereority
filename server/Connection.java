package server;

import common.*;
import common.messages.LoginMessage;

import java.lang.reflect.Array;
import java.net.*;
import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;

class Connection extends Thread {
    /*
	 * login was handled by NetworkListener.java since this thread was created,
	 * NetworkListener.java: 1. accepted a TCP connection from a client 2. read
	 * a login message from the TCP port (obviously not the accept port) 3.
	 * found a valid userid password pair in the login message 4. created this
	 * thread 5. called Connection.run(socket) where socket = the socket over
	 * which login happened NetworkListener.java only read the login messages,
	 * it sent no responses to the client hence, Connection.java must
	 * acknowledge login
	 */

     // handle to the ServerGameEngine
    ServerGameEngine           gameengine;

    String                      username;

    private Selector		selector;
    private SelectionKey	readwritesockkey;

    private Socket		sock;
    private SocketChannel	sockchannel;
    private ObjectInputStream	oistream;

    private DatagramSocket	dsock;
    private DatagramChannel	dsockchannel;
    private ObjectOutputStream  dsockoutputstream;

    private SocketChannel       gamechannel;


    public Connection(String uname, ServerGameEngine sge, SocketChannel sc, ObjectInputStream ois)
    {
        username = uname;
        gameengine = sge;
	    sockchannel = sc;
	    oistream = ois;
    }
    
    public void run()
    {
	    System.out.println("Connection.run()");
        try {
	        /*
			 * Setup first create a udp socket
			 */
	        // create a udp socket
	        dsockchannel = DatagramChannel.open();
	        dsockchannel.socket().bind(new InetSocketAddress("localhost",0));

	        // send login success mesage + udp port number
	        int localport = dsockchannel.socket().getLocalPort();
	        byte [] bytes = LoginMessage.getLoginSuccessMessage(localport);
	        ObjectOutputStream sockoutputstream = new ObjectOutputStream(sockchannel.socket().getOutputStream());
	        sockoutputstream.writeObject(bytes);

	        // get response to success message
	        Object obj = oistream.readObject();
	        int numBytes = Array.getLength(obj);
	        bytes = new byte[numBytes];
	        for (int i=0; i<numBytes; i++)
		    bytes[i] = Array.getByte(obj,i);
	        String message = LoginMessage.getMessageString(bytes);
	        System.out.println(message);

            // connect udp socket to client
            if (!LoginMessage.isLoginSuccessMessage(bytes))
            {
                System.out.println("Client could not connect");
            }
            else
            {
                int udp_remoteport = LoginMessage.getPort(bytes);
                dsockchannel.socket().connect(new InetSocketAddress(sockchannel.socket().getInetAddress(),udp_remoteport));

                // register the client with the game engine
                // gived it handles to the outputs streams of the sockets so it
				// can send on it's own
                gameengine.newClient(username,sockoutputstream,dsockchannel);

                // create the selector for polling the channels
                selector = Selector.open();
                // set channels to non-blocking and register them
        	    sockchannel.configureBlocking(false);
        	    readwritesockkey = sockchannel.register( selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE );
        	    dsockchannel.configureBlocking(false);
        	    dsockchannel.register( selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE );

                /*
				 * Everything is set up Sleep on the selector
				 */
                 while (true)
                 {
                     // wait for an event
                     selector.select();
                     // get list of selection keys with pending events
                     Iterator it = selector.selectedKeys().iterator();
                     // process each key at a time
                     while (it.hasNext())
                     {
                         // get the selection key
                         SelectionKey selKey = (SelectionKey)it.next();
                         // remove it from list
                         it.remove();
                         processSelectionKey(selKey);
                     }
                 }

            }
                
            dsockchannel.close();
            sockchannel.close();
        }
        catch (Exception e){
	        System.out.println("Connection.java: Damn! ");
	        e.printStackTrace();
	        System.exit(1);
	    }
    }
    private void processSelectionKey(SelectionKey selKey){
    }
}
	
