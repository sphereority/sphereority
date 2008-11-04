package common.messages;

import java.nio.ByteBuffer;

/**
 * 
 * @author rlagman
 */
public class PlayerMotionMessage implements Packetizable {
    /**
     *  The header for the message.
     */
    private Header header;

    /**
     * The id of the player who moved.
     */
    private int playerId;

    /**
     * The x co-ordinate of where the player moved.
     */
    private float x;

    /**
     * The y co-ordinate of where the player moved.
     */
    private float y; 

    /**
     *  Constructor - Creates a new instance.
     */
    public PlayerMotionMessage() {
        header = new UDPHeader(MessageType.PlayerMotion,255);
    }

	/**
	 * Reads the packet information and reconstructs
	 * the Packetizable object.
	 * @param data The raw byte data that was sent.
	 * @return The DeathMessage populated with the input data.
	 */
	public Packetizable readPacketData(byte[] data) {
        // Wrap the stream of bytes into a buffer
       
        ByteBuffer buffer = ByteBuffer.wrap(data);

		
        
        if(MessageAnalyzer.getMessageType(buffer.get()) != MessageType.DeathMessage)
		    return null;

		// Process the information to create the object.
		
		return this;
	}
	
	/**
	 * Creates an array of bytes to be sent across the network
	 * that represents a Packetizable object.
	 * @return A byte representation of the Packetizable object.
	 */
	public byte[] createPacketData() {
        byte[] data = new byte[MTU];
       
        
        // Create a buffer the size of the maximum transmission unit
		ByteBuffer buffer = ByteBuffer.wrap(data);
        
        
        
        // Create the packet by processing the data

        // Return
        return data;
	}

    /**
     *  Retrieves the type of message to be sent.
     *  @return The type of message of this packet.
     */
    public MessageType getMessageType() {
        return header.getMessageType();
    }
}
