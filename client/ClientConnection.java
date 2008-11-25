package client;

import common.*;
import common.messages.*;

import java.awt.event.*;
//import javax.swing.*;
import java.net.*;
//import java.io.*;
import java.nio.*;
import java.nio.channels.*;
//import java.nio.charset.Charset;
import java.util.*;

/**
 * Client connection to the server and other clients.
 */
public class ClientConnection implements ActionListener, Constants {
    private GameEngine engine;
    private Selector selector;
    private SocketChannel sockChannel;
//    private DatagramChannel serverUDPChannel;
//    private DatagramChannel mcastChannel;
//    private TreeMap<Integer,MulticastSocket> clientMcastSockets;
    private javax.swing.Timer timer;

    private MulticastSocket mSocket;
    private MulticastReader reader;
    private Pipe pipe;
    private Pipe.SourceChannel source;
    
    private InetAddress myMCastGroup;
    private int myMCastPort;

    /**
     * Creates a client connection to the server and other clients.
     * @param engine
     * @param serverName
     * @param port
     * @param userName
     */
	public ClientConnection(GameEngine engine)  {
        try {
            this.engine = engine;
            this.sockChannel = SocketChannel.open();
            //this.serverUDPChannel = DatagramChannel.open();
            //this.mcastChannel = DatagramChannel.open();
            
            this.selector = Selector.open();
//            this.clientMcastSockets = new TreeMap<Integer,MulticastSocket>();
        } catch (Exception ex) {
            System.err.println("Could not connect to server!");
            ex.printStackTrace();
        }     
    }
	
	public void setGameEngine(GameEngine engine)
	{
		this.engine = engine;
	}

    /**
     * Logs into the specified server using the userName.
     */
    public int loginToServer(String serverName, String userName) throws Exception {
        int playerId = -1;
        // Create a ByteBuffer
        ByteBuffer buffer = ByteBuffer.allocate(4096);
        buffer.flip();
        /*
        Avoid the server for now.  Just connect to same Multicast Address
        // Connect to the specified server
        sockChannel.connect(new InetSocketAddress(serverName,DEFAULT_PORT));
        
        // Create and send a login message
        byte[] loginMessage = LoginMessage.getLoginMessage(userName,"");
        System.out.println(LoginMessage.getMessageString(loginMessage));
        sockChannel.write(ByteBuffer.wrap(loginMessage));
        
        // Receive a login success/failure
        sockChannel.read(buffer);
        byte[] loginSuccess = buffer.array();

        // Check if we successfully logged in
        if(LoginMessage.isLoginSuccessMessage(loginSuccess)) {
            int port = LoginMessage.getPort(loginSuccess);
            playerId = LoginMessage.getPlayerId(loginSuccess);
            System.out.printf("Port: %d\n", port);

            // create datagram channel & connect to rem port
            serverUDPChannel.configureBlocking(false);
            serverUDPChannel.connect(new InetSocketAddress(serverName,port));

            // get localport of datagram socket
            int localport = serverUDPChannel.socket().getLocalPort();
            System.out.printf("UDP local port: %d\n", localport);
            // Register the UDP Channel with the selector
            serverUDPChannel.register(selector,serverUDPChannel.validOps());
		    
            // send success message to send port number to server
		    loginSuccess = LoginMessage.getLoginSuccessMessage((byte)playerId,MCAST_ADDRESS,localport);
		    sockChannel.write(ByteBuffer.wrap(loginSuccess));

            // Get your multicast address and port
            myMCastGroup = InetAddress.getByName(MCAST_ADDRESS);
            myMCastPort = MCAST_PORT;
            
            // Set up the multicast parts of the application
            myMCastGroup = InetAddress.getByName(MCAST_ADDRESS);
            myMCastPort  = MCAST_PORT;
            mSocket = new MulticastSocket(MCAST_PORT);
            mSocket.joinGroup(myMCastGroup);

            // Create a pipe to write back to the connection            
            pipe = Pipe.open();
            source = pipe.source();

            source.configureBlocking(false);
            source.register(selector,SelectionKey.OP_READ);

            // Notify everyone that we have joined
            PlayerJoinMessage message = new PlayerJoinMessage((byte)engine.localPlayer.getPlayerId(),
                                                              myMCastGroup,
                                                              engine.localPlayer.getPlayerName());
            sendMessage(message);

            reader = new MulticastReader(mSocket,pipe.sink());
            reader.start();
        }
        else {
            System.err.println("Unable to log in!");
        }*/

        // Get your multicast address and port
        myMCastGroup = InetAddress.getByName(MCAST_ADDRESS);
        myMCastPort = MCAST_PORT;
            
        // Set up the multicast parts of the application
        mSocket = new MulticastSocket(MCAST_PORT);
        mSocket.joinGroup(myMCastGroup);
        if (mSocket.getReceiveBufferSize() < 1024*128)
        	mSocket.setReceiveBufferSize(1024*128);
        System.out.printf("Multicast port number: %d\n", mSocket.getLocalPort());

        // Create a pipe to write back to the connection            
        pipe = Pipe.open();
        source = pipe.source();

        source.configureBlocking(false);
        source.register(selector,SelectionKey.OP_READ);

        // Notify everyone that we have joined
        //PlayerJoinMessage message = new PlayerJoinMessage((byte)engine.localPlayer.getPlayerID(),
        //                                                  new InetSocketAddress(myMCastGroup,myMCastPort),
        //                                                  engine.localPlayer.getPlayerName());
        //sendMessage(message);

        reader = new MulticastReader(mSocket,pipe.sink());
        reader.start();
    
        return playerId;
    }

    /**
     * Checks if messages have been sent to the client.
     */
    public void checkMessages() {
        // Wait to receive a message
        try {
        	selector.selectNow();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        
        // Go through all the received messages
        Iterator<SelectionKey> it = selector.selectedKeys().iterator();
        ByteBuffer receivedBuffer = ByteBuffer.allocate(4096);
      
        // Do a max of 20 messages at a time for efficiency reasons
        for(int i = 0; it.hasNext() && i < 20; i++) {
            SelectionKey selKey = it.next();
            
            try {
                receivedBuffer.clear();
                processMessage(selKey, receivedBuffer);
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
            // Remove it to indicate it is processed
            it.remove();
            
            // Reset the buffer for further use
            receivedBuffer.clear();
            receivedBuffer.flip();
        }
    }

    
    /**
     * Processes a SelectorKey (Message) that has come in.
     * @param selKey Key that is associated with a message.
     * @param buffer The buffer to use to store the messages.
     */
    protected void processMessage(SelectionKey selKey, ByteBuffer buffer) throws Exception {
        // Since the ready operations are cumulative,
        // need to check readiness for each operation    
        if (selKey.isValid() && selKey.isReadable()) {
            // Get channel with connection request
            source.read(buffer);
            buffer.rewind();

            // Get the message from the buffer
            Message message = MessageAnalyzer.getMessage(buffer);
            
            if (message == null)
            	return;
            
            if(message.getPlayerId() == engine.localPlayer.getPlayerID())
                return;

            switch(message.getMessageType()) {
                case PlayerMotion:
                    engine.processPlayerMotion((PlayerMotionMessage)message);
                    System.out.printf("PlayerMotion: %d moved to\t(%.2f,%.2f)\n", message.getPlayerId(), ((PlayerMotionMessage)message).getPosition().getX(), ((PlayerMotionMessage)message).getPosition().getY());
                    break;
                case PlayerJoin:
                    PlayerJoinMessage msg = (PlayerJoinMessage) message;
                    System.out.println("PlayerJoin: " + msg.getName() + " wants to join");
                    engine.processPlayerJoin(msg);
                    registerPlayer(msg.getPlayerId(),msg.getAddress());
                    break;
                default:
                    System.out.println("Got Undefined Message");
                    break;
            }
        }
    }

    /**
     * Sends a message through the multicast socket.  Client does not subscribe to their
     * own multicast group.
     * @param The message to be sent.
     */
    public void sendMessage(Message message) throws Exception {
        byte[] byteMessage = message.getByteMessage();
        DatagramPacket packet = new DatagramPacket(byteMessage,byteMessage.length,
                                                   myMCastGroup,myMCastPort);
        mSocket.send(packet);
    }

    /**
     * 
     */
    protected void registerPlayer(int playerId, InetSocketAddress mcastAddress) {
        
    }

    /**
     *  
     */
    public void actionPerformed(ActionEvent e) {
        try {
            checkMessages();

            // Send motion message for this player
//            sendMessage(new PlayerMotionMessage((byte)engine.localPlayer.getPlayerID(),
//                                            engine.localPlayer.getPosition(),
//                                            engine.localPlayer.getVelocity(),
//                                            (float)System.currentTimeMillis()));
            PlayerMotionMessage pmm = engine.localPlayer.getMotionPacket(engine.currentTime);
            //System.out.printf("Sending PlayerMotionMessage\n");
            sendMessage(pmm);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        
    }

    /**
     *
     */
    public void start() {
        timer = new javax.swing.Timer(TIMER_TICK, this);
        timer.start();
		timer.setCoalesce(true);
    }
}