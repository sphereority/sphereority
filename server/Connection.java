package server;

import common.*;
import common.messages.*;
import common.messages.LoginMessage;

import java.net.*;
import java.nio.*;
import java.nio.channels.*;
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

     // handle to the ServerGameEngine
    ServerGameEngine           gameengine;

    String                      username;

    private Selector		selector;
    private SelectionKey	readsocket_key;

    private Socket			sock;
    private SocketChannel	sockchannel;
    private SelectionKey    socket_key;

    private DatagramSocket	dsock;
    private DatagramChannel	dsockchannel;
    private SelectionKey    dgram_socket_key;

    private SocketChannel   gamechannel;


    Connection(String uname, ServerGameEngine sge, SocketChannel sc){
        username = uname;
        gameengine = sge;
	    sockchannel = sc;
    }
    public void run(){
	    System.out.println("Connection.run()");
        try {
	        /*
	         * Setup
	         *     first create a udp socket
	         */
	        // create a udp socket
	        dsockchannel = DatagramChannel.open();
	        dsockchannel.socket().bind(new InetSocketAddress("localhost",0));

	        // send login success mesage + udp port number
	        int localport = dsockchannel.socket().getLocalPort();
	        byte [] bytes = LoginMessage.getLoginSuccessMessage(localport);
	        ByteBuffer buf = ByteBuffer.allocate(4096);
	        buf.put(bytes);
	        buf.flip();
	        int numwritten = sockchannel.write(buf);
	        System.out.printf("Connection.java: first write: number of bytes written: %d\n", numwritten);
	        // get response to success message
	        buf.clear();
	        int numread = sockchannel.read(buf);
	        System.out.printf("Connection.java: first read: bytes read: %d\n", numread);
	        bytes = new byte[numread];
	        buf.flip();
	        buf.get(bytes);
	        String message = LoginMessage.getMessageString(bytes);
	        System.out.println(message);

            // connect udp socket to client
            if (!LoginMessage.isLoginSuccessMessage(bytes)){
                System.out.println("Client could not connect");
            }
            else {
                int udp_remoteport = LoginMessage.getPort(bytes);
                dsockchannel.socket().connect(new InetSocketAddress(sockchannel.socket().getInetAddress(),udp_remoteport));

                // register the client with the game engine
                // gived it handles to the outputs streams of the sockets so it can send on it's own
                gameengine.newClient(username,sockchannel,dsockchannel);

                // create the selector for polling the channels
                selector = Selector.open();
                // set channels to non-blocking and register them
        	    sockchannel.configureBlocking(false);
        	    socket_key = sockchannel.register( selector, SelectionKey.OP_READ );
                //socket_key.attach(oistream);
        	    dsockchannel.configureBlocking(false);
        	    dgram_socket_key = dsockchannel.register( selector, SelectionKey.OP_READ );
                dgram_socket_key.attach(new String("datagram"));

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
                     System.out.print("NUmber of keys ready: ");
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
                return;
                //System.exit(0);
            }
            catch (Exception e){
                e.printStackTrace();
            }

            /*
            if (selKey.channel() == sockchannel){
                try {
                    Message message = (Message) oistream.readObject();
                    System.out.println("selKey.getChannel() == sockchannel");
                    System.exit(0);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }*/
        }
    }
}
	
