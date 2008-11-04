package common.messages;

/**
 * Interface - Packetizable
 * @author Raphael Lagman
 * 
 * This interface must be implemented in order to send
 * objects across the network.  Rather than serializing the
 * object and sending the bytes, we are sending the raw data
 * with the following header:
 *
 * Each class implementing this interface will need to handle
 * how to interpret the remaining data.  In other words,
 * how to interpret the data format of the byte.
 */
public interface Packetizable {
    
    /**

     * Constant - The maximum amount of data that can be sent in a message.
  
     */
    public final int MTU = 1500;
    
	/**
	 * Reads the packet information and reconstructs
	 * the Packetizable object.
	 * @param data The raw byte data that was sent.
	 * @return A Packetizable object that represents a message.
	 */
	public Packetizable readPacketData(byte[] data);
	
	/**
	 * Creates an array of bytes to be sent across the network
	 * that represents a Packetizable object.
	 * @return A byte representation of the Packetizable object.
	 */
	public byte[] createPacketData();

    /**
     * Retrieves the message type of the Packetizable message.
     */
    public MessageType getMessageType();
}
