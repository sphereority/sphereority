package common.messages;

import common.Constants;
import java.nio.ByteBuffer;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginMessage extends Message implements Constants {
	public static Logger logger = Logger.getLogger(CLIENT_LOGGER_NAME);

    private String userName;    

    private InetSocketAddress address;

    private boolean isAck;
    
    /**
     * Constructor - Creates a new LoginMessage.
     * @param playerId The id of a player.
     * @param userName The requested user name.
     * @param address The address that should be used for further communication
     * @param isAck An acknowledgement login message
     */
    public LoginMessage(byte playerId, String userName, InetSocketAddress address, boolean isAck) {
        super(MessageType.Login, playerId, ChatMessageLength);
        this.userName   = userName;
        this.address    = address;
        this.isAck      = isAck;
    }

    /**
     * Constructor - Creates a new LoginMessage.
     * @param header Representation of a Header in bytes.
     * @param data Representation of the data portion in bytes.
     */
    public LoginMessage(byte[] header, byte[] data) {
        // Create the Message superclass
        super(header,LoginMessageLength);
        
        // Wrap the stream of bytes into a buffer       
        ByteBuffer buffer = ByteBuffer.wrap(data);
		
		// Process the information to create the object.
        isAck = buffer.getShort() == 1;
        
        byte messageLength = buffer.get();
        
        byte[] messageArray = new byte[messageLength];

        // Recreate the string as an array of characters
        for(int i = INIT; i < messageLength; i++)      
            messageArray[i] = buffer.get();
        
        // Store the message
        this.userName = new String(messageArray);
        
        // Get the network address
        messageLength = buffer.get();
        messageArray = new byte[messageLength];
        
        for(int i = INIT; i < messageLength; i++)      
            messageArray[i] = buffer.get();
        try {
        this.address = new InetSocketAddress(InetAddress.getByAddress(messageArray),
                                             buffer.getInt());
        } catch(Exception ex) { ex.printStackTrace(); }
    }
    
    /**
	 * Creates an array of bytes to be sent across the network
	 * that represents a Message object.
	 * @return A byte representation of the Message object.
	 */
	public byte[] getByteMessage() throws Exception {
        // Get the header
        byte[] header = getByteHeader();        
        byte[] message = new byte[header.length + dataLength];
        
        ByteBuffer buffer = ByteBuffer.wrap(message);
        buffer.put(header);
        
        // Place the header information
        if(isAck) buffer.putShort((short)1); else buffer.putShort((short)0);
        
        // Place the contents of this data
        buffer.put((byte)userName.length());
        buffer.put(userName.getBytes());
        
        byte[] inetAddress = address.getAddress().getAddress();
        buffer.put((byte)inetAddress.length);
        buffer.put(inetAddress);
        buffer.putInt(address.getPort());
        
        // Return
        return message;
	}
    
    public String getPlayerName() {
        return userName;
    }

    public InetSocketAddress getAddress() {
        return address;    
    }
    
    public boolean isAck() {
        return isAck;
    }
}
