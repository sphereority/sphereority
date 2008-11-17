package common.messages;

import java.nio.ByteBuffer;

/**
 * LoginMessage - Logs a player into a server.
 * @author rlagman
 */
public class LoginMessage extends Message implements MessageConstants {
    private String userName;    
    private String password;
    private boolean success;
    
    /**
     * Constructor - Creates a new LoginMessage.
     * @param playerId The id of the player sending the message.
     */
    public LoginMessage(byte playerId, String userName, String password, boolean success) {
        super(MessageType.LoginMessage, playerId, LoginMessageLength);
        this.userName = userName;
        this.password = password;
        this.success  = success;
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
        int messageLength = (int) buffer.get();
        char[] messageArray = new char[messageLength];

        // Get whether this was a success
        this.success = (buffer.get() == 1);       

        // Recreate the string as an array of characters
        for(int i = INIT; i < messageLength; i++)      
            messageArray[i] = buffer.getChar();
        
        // Store the userName
        this.userName = String.copyValueOf(messageArray);

	    // Process the information to create the object.
        messageLength = (int) buffer.get();
        messageArray = new char[messageLength];

        // Recreate the string as an array of characters
        for(int i = INIT; i < messageLength; i++)      
            messageArray[i] = buffer.getChar();
        
        // Store the userName
        this.password = String.copyValueOf(messageArray);
    }
    
    /**
	 * Creates an array of bytes to be sent across the network
	 * that represents a Packetizable object.
	 * @return A byte representation of the Message object.
	 */
	public byte[] getByteMessage() throws Exception {
         // Get the header
        byte[] header = getByteHeader();        
        byte[] message = new byte[header.length + dataLength];
        
        // Place the header information
        ByteBuffer buffer = ByteBuffer.wrap(message);
        buffer.put(header);
        
        // Place the contents of this data
        buffer.put((success ? (byte)1 : (byte)0));
        buffer.put((byte)userName.length());
        buffer.put(userName.getBytes(CharacterEncoding));
        buffer.put((byte)password.length());
        buffer.put(password.getBytes(CharacterEncoding));
        
        // Return
        return message;
	}

    /**
     * Retrieves the user name.
     */
    public String getUserName() {
        return userName;
    }	

    /**
     * Retrieves password information.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Retrieves whether login is successful.
     */
    public boolean getSuccess() {
        return success;    
    }
}
