package server;

import common.*;
import common.messages.*;
import common.messages.LoginMessage;

import java.net.*;
import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;

class Connection extends Thread implements Constants{
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

     // handle to the ServerGameEngine
    private ServerGameEngine        gameengine;
    private String                  username;
    private Socket					socket;
    private BufferedInputStream		istream;
    private BufferedOutputStream	ostream;
    private byte []					bytes;

    Connection(String username, ServerGameEngine gameengine, Socket socket, BufferedInputStream istream){
        this.username = username;
        this.gameengine = gameengine;
	    this.socket = socket;
	    this.istream = istream;
    }
    public void run(){
	    System.out.println("Connection.run()");
        try {
	        /*
	         * Setup
	         */
        	//get outputstream from socket
        	ostream = new BufferedOutputStream(socket.getOutputStream());
        	//register client with game engine
        	byte playerid = gameengine.newClient(username);
	        
	        // send login success mesage
        	byte [] bytes = LoginMessage.getLoginSuccessMessage(playerid);
	        System.out.print("Connection.java: First Success Message:");
	        System.out.println(LoginMessage.getMessageString(bytes));
	        ostream.write(bytes);
	        //System.out.printf("Connection.java: first write: number of bytes written: %d\n", numwritten);
	        // get response to success message
	        int numread = istream.read(bytes);
	        System.out.printf("Connection.java: first read: bytes read: %d\n", numread);
	        String message = LoginMessage.getMessageString(bytes);
	        System.out.println(message);

            if (!LoginMessage.isLoginSuccessMessage(bytes)){
                System.out.println("Client could not connect");
            }
            else {
                // register the client's socketchannel with the game engine
                gameengine.clientChannels(playerid, sockchannel);

                // create the selector for polling the channels
                selector = Selector.open();
                // set channels to non-blocking and register them
        	    sockchannel.configureBlocking(false);
        	    socket_key = sockchannel.register( selector, SelectionKey.OP_READ );
                //socket_key.attach(oistream);
        	    dsockchannel.configureBlocking(false);
        	    dgram_socket_key = dsockchannel.register( selector, SelectionKey.OP_READ );
                //dgram_socket_key.attach(new String("datagram"));

                /*
                 * Everything is set up
                 * Sleep on the selector
                 */
                 int blah = 1;
                 while (blah < 3) {
                     System.out.printf("Connection.java: about to wait: %d\n",blah);
                     blah++;
                     // wait for an event
                     int numkeys = selector.select();
                     System.out.print("Number of keys ready: ");
                     System.out.println(numkeys);
                     // get list of selection keys with pending events
                     Set keySet = selector.selectedKeys();
                     Iterator it = keySet.iterator();
                     // process each key at a time
                     while (it.hasNext()){
                         System.out.println("HERE");
                         // get the selection key
                         SelectionKey selKey = (SelectionKey)it.next();
                         //remove it from list
                         it.remove();
                         processSelectionKey(selKey);
                     }
                     keySet.clear();
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
        if (selKey.isValid() && selKey.isReadable()){
            try {
                SocketChannel channel = (SocketChannel) selKey.channel();
                ByteBuffer buf = ByteBuffer.allocate(1024);
                byte [] bytes = new byte[1024];
                int numread = channel.read(buf);
                System.out.printf("Number of bytes read: %d\n", numread);
                buf.rewind();
                buf.get(bytes);
                Message message = MessageAnalyzer.getMessage(bytes);
                if (channel == sockchannel)
                    gameengine.newTCPMessage(message);
                else
                    gameengine.newUDPMessage(message);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
