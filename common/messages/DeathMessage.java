package common.messages;

import java.nio.ByteBuffer;

/**
 * DeathMessage - Notifies that a player has killed another player.
 * @author rlagman
 */
public class DeathMessage extends Message implements MessageConstants {
    private byte killedBy;    
    private byte killed;
    
    /**
     * Constructor - Creates a new DeathMessage.
     * @param playerId The id of the player sending the message.
     * @param killed  The id of the player who was killed.
     * @param killedBy The id of the player who killed the other player.
     */
    public DeathMessage(byte playerId, byte killedBy) {
        super(MessageType.Death, playerId, DeathMessageLength);
        this.killed    = playerId;
        this.killedBy  = killedBy;
    }

    /**
     * Constructor - Creates a new DeathMessage.
     * @param header Representation of a Header in bytes.
     * @param data Representation of the data portion in bytes.
     */
    public DeathMessage(byte[] header, byte[] data) {
        // Create the Message superclass
        super(header,DeathMessageLength);
        
        // Wrap the stream of bytes into a buffer       
        ByteBuffer buffer = ByteBuffer.wrap(data);
		
		// Process the information to create the object.
        killed   = buffer.get();
        killedBy = buffer.get();
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
        buffer.put(killed);
        buffer.put(killedBy);
        
        // Return the fully created message
        return message;
	}	
}
