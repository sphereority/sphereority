package common.messages;

import java.nio.ByteBuffer;

/**
 * 
 * @author rlagman
 */
public class ChatMessage extends Message implements MessageConstants {
    /**
     * The type of message that is being sent.
     */
    private byte destId;    

    /**
     * The message to be sent.
     */
    private String message;

    /**
     * Constructor - Creates a new ChatMessage.
     * @param playerId The id of a player.
     * @param destId The id of where the message should be sent.
     * @param message The message to be delivered.
     */
    public ChatMessage(byte playerId, byte destId, String message) {
        super(MessageType.ChatMessage, playerId, ChatMessageLength);
        this.destId   = destId;
        this.message  = message;
    }

    /**
     * Constructor - Creates a new ChatMessage.
     * @param header Representation of a Header in bytes.
     * @param data Representation of the data portion in bytes.
     */
    public ChatMessage(byte[] header, byte[] data) {
        // Create the Message superclass
        super(header,ChatMessageLength);
        
        // Wrap the stream of bytes into a buffer       
        ByteBuffer buffer = ByteBuffer.wrap(data);
		
		// Process the information to create the object.
        byte destId        = buffer.get();
        int messageLength = (int) buffer.get();
        
        char[] messageArray = new char[messageLength];

        // Recreate the string as an array of characters
        for(int i = INIT; i < messageLength; i++)      
            messageArray[i] = buffer.getChar();
        
        // Store the message
        this.message = String.copyValueOf(messageArray);
    }
    
    /**
	 * Creates an array of bytes to be sent across the network
	 * that represents a Packetizable object.
	 * @return A byte representation of the Packetizable object.
	 */
	public byte[] getByteMessage() throws Exception {
        // Get the header
        byte[] header = getByteHeader();        
        byte[] message = new byte[header.length + dataLength];
        
        // Place the header information
        ByteBuffer buffer = ByteBuffer.wrap(message);
        buffer.put(header);
        
        // Place the contents of this data
        buffer.put(destId);
        buffer.put((byte)this.message.length());
        buffer.put(this.message.getBytes(CharacterEncoding));
        
        // Return
        return message;
	}
    
    /**
     * Retrieves the id of the destination for this message.
     * @return An id.
     */
    public byte getDestId() {
        return destId;
    }

    /**
     * Retrieves the message that was sent by the sender.
     * @return The message sent by the sender.
     */
    public String getMessage() {
        return message;    
    }
	
}
