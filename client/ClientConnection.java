package client;

import common.*;
import common.messages.*;

import java.net.*;
import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.*;

/**
 * Client connection to the server and other clients.
 */
public class ClientConnection {
    private GameEngine engine;
    private Selector selector;
    private SocketChannel sockChannel;
    private DatagramChannel serverUDPChannel;
    private MulticastSocket myMcastSocket;
    private TreeMap<Integer,MulticastSocket> clientMcastSockets;
    
	public ClientConnection(GameEngine engine, String serverName, int port, String userName) throws Exception {
        this.engine = engine;
        this.sockChannel = SocketChannel.open();
        selector = Selector.open();
        clientMcastSockets = new TreeMap<Integer,MulticastSocket>();
        loginToServer(serverName, port, userName);        
    }

    /**
     * Logs into the specified server using the userName.
     */
    public void loginToServer(String serverName, int serverPort, String userName) throws Exception{
        // Create a ByteBuffer
        ByteBuffer buffer = ByteBuffer.allocate(4096);
        buffer.rewind();

        // Connect to the specified server
        sockChannel.connect(new InetSocketAddress(serverName,serverPort));
        
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
            System.out.printf("Port: %d\n", port);

            // create datagram channel & connect to rem port
            serverUDPChannel = DatagramChannel.open();
            serverUDPChannel.socket().connect(new InetSocketAddress(sockChannel.socket().getInetAddress(),port));
            // get localport of datagram socket
            int localport = serverUDPChannel.socket().getLocalPort();
            System.out.printf("UDP local port: %d\n", localport);
            // Register the UDP Channel with the selector
            serverUDPChannel.register(selector,serverUDPChannel.validOps());
		    
            // send success message to send port number to server
		    loginSuccess = LoginMessage.getLoginSuccessMessage(localport);
		    sockChannel.write(ByteBuffer.wrap(loginSuccess));
        }    
    }

    /**
     * Checks if messages have been sent to the client.
     */
    public void checkReceivedMessages() {
        // Wait to receive a message
        // If no messages just exit out
        try {
        	selector.select(10);
        }
        catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
        
        // Go through all the received messages
        Iterator<SelectionKey> it = selector.selectedKeys().iterator();
        ByteBuffer receivedBuffer = ByteBuffer.allocate(4096);
        
        // Do a max of 20 messages at a time for efficiency reasons
        for(int i = 0; i < 20 && it.hasNext(); i++) {
            SelectionKey selKey = it.next();
            
            // Remove it to indicate it is processed
            it.remove();
            
            try {
                processMessage(selKey,receivedBuffer);
            }
            catch (Exception ex) {
                System.err.println(ex.getMessage());
            }
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
        if (selKey.isValid() && selKey.isConnectable()) {
            // Get channel with connection request
            DatagramChannel dChannel = (DatagramChannel)selKey.channel();
            dChannel.read(buffer);
            
            // Get the message from the buffer
            Message message = MessageAnalyzer.getMessage(buffer);
            
            // Handle the messages
            if(message instanceof PlayerMotionMessage) {
                engine.processPlayerMotion((PlayerMotionMessage)message);
            }
        }
    }

    /**
     * Sends a message through the multicast socket.
     * @param The message to be sent.
     */
    public void sendMessage(Message message) throws Exception {
        byte[] byteMessage = message.getByteMessage();
        myMcastSocket.send(new DatagramPacket(byteMessage, byteMessage.length));
    }
}