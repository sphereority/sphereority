package common.messages;

import common.Constants;
import java.nio.ByteBuffer;
import java.util.logging.Logger;

/**
 * HealthUpdateMessage - Notifies that a player's health has changed.
 * @author rlagman
 */
public class HealthUpdateMessage extends Message implements MessageConstants, Constants {
	public static Logger logger = Logger.getLogger(CLIENT_LOGGER_NAME);

    private byte newHealth;    

    /**
     * Constructor - Creates a new HealthUpdateMessage.
     * @param playerId The id of the player sending the message.
     * @param newHealth The new health amount for the player.
     */
    public HealthUpdateMessage(byte playerId, byte newHealth) {
        super(MessageType.HealthUpdate, playerId, HealthUpdateLength);
        this.newHealth = newHealth;
    }

    /**
     * Constructor - Creates a new HealthUpdateMessage.
     * @param header Representation of a Header in bytes.
     * @param data Representation of the data portion in bytes.
     */
    public HealthUpdateMessage(byte[] header, byte[] data) {
        // Create the Message superclass
        super(header,HealthUpdateLength);
        
        // Wrap the stream of bytes into a buffer       
        ByteBuffer buffer = ByteBuffer.wrap(data);
		
		// Process the information to create the object.
        this.newHealth   = buffer.get();
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
        buffer.put(newHealth);
        
        // Return the fully created message
        return message;
	}
    
    public byte getNewHealth() {
        return newHealth;
    }
}
