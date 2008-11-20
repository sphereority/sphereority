package common.messages;

import java.nio.ByteBuffer;

/**
 * MapChangeMessage - Notifies that the map is changing.
 * @author rlagman
 */
public class MapChangeMessage extends Message implements MessageConstants {
    
    /**
     * Constructor - Creates a new MapChangeMessage.
     * @param playerId The id of the player sending the message.
     */
    public MapChangeMessage(byte playerId) {
        super(MessageType.ChatMessage, playerId, MapChangeLength);
    }

    /**
     * Constructor - Creates a new MapChangeMessage.
     * @param header Representation of a Header in bytes.
     * @param data Representation of the data portion in bytes.
     */
    public MapChangeMessage(byte[] header, byte[] data) {
        // Create the Message superclass
        super(header,MapChangeLength);
        
        // Wrap the stream of bytes into a buffer       
        ByteBuffer buffer = ByteBuffer.wrap(data);
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
        
        // Place the header information
        ByteBuffer buffer = ByteBuffer.wrap(message);
        buffer.put(header);
        
        // Return the fully created message
        return message;
	}	
}
