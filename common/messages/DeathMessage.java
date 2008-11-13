package common.messages;

import java.nio.ByteBuffer;


/**
 * 
 * @author rlagman
 */
public class DeathMessage extends Message implements MessageLengths {

    /**
     * The id of the player who killed.
     */
    private byte killedByPlayerId;

    /**
     * The id of the player who was killed.
     */
    private byte killedPlayerId;    

    /**
     * Constructor - Creates a new DeathMessage.
     */
    public DeathMessage(byte killedByPlayerId, byte killedPlayerId) {
        super(MessageType.DeathMessage);
        this.killedByPlayerId = killedByPlayerId;
        this.killedPlayerId   = killedPlayerId;
        dataLength = DeathMessageLength;    
    }

    /**
     * Retrieve the ID of the player who was killed.
     */
    public byte getKilled() {
        return killedPlayerId;
    }

    /**
     * Retrieves the ID of the player who killed.
     */
    public byte getKiller() {
        return killedByPlayerId;
    }

	/**
	 * Reads the packet information and reconstructs
	 * the Packetizable object.
	 * @param data The raw byte data that was sent.
	 * @return The DeathMessage populated with the input data.
	 */
	public static DeathMessage readMessage(byte[] data) {
        // Wrap the stream of bytes into a buffer
       
        ByteBuffer buffer = ByteBuffer.wrap(data);

		
        
		// Process the information to create the object.
        byte killedByPlayerId = buffer.get();
        byte killedPlayerId   = buffer.get();

		return new DeathMessage(killedByPlayerId,killedPlayerId);
	}
	
	/**
	 * Creates an array of bytes to be sent across the network
	 * that represents a Packetizable object.
	 * @return A byte representation of the Packetizable object.
	 */
	public byte[] getByteMessage() {
        // Get the header
        byte[] header = getByteHeader();
        
        byte[] message = new byte[header.length + dataLength];
        
        // Place the header information
        ByteBuffer buffer = ByteBuffer.wrap(message);
        
        
        buffer.put(header);
        
        // Place the contents of this data
        buffer.put(killedByPlayerId);
        buffer.put(killedPlayerId);
        
        // Return
        return message;
	}
}
