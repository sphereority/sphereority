package common.messages;

import java.nio.ByteBuffer;
import java.net.InetSocketAddress;
import java.net.InetAddress;

/**
 * PlayerJoinMessage - Notifies that a player is joining the game.
 * @author rlagman
 */
public class PlayerJoinMessage extends Message implements MessageConstants {
    private InetSocketAddress mcastAddress;
    
    /**
     * Constructor - Creates a new MulticastGroupMessage.
     * @param playerId The id of the player sending the message.
     */
    public PlayerJoinMessage(byte playerId, InetSocketAddress mcastAddress) {
        super(MessageType.PlayerJoin, playerId, PlayerJoinLength);
        this.mcastAddress = mcastAddress;
    }

    /**
     * Constructor - Creates a new MulticastGroupMessage.
     * @param header Representation of a Header in bytes.
     * @param data Representation of the data portion in bytes.
     */
    public PlayerJoinMessage(byte[] header, byte[] data) {
        // Create the Message superclass
        super(header,PlayerJoinLength);
        
        // Wrap the stream of bytes into a buffer       
        ByteBuffer buffer = ByteBuffer.wrap(data);
		
		// Process the information to create the object.
        int messageLength = (int) buffer.get();
        byte[] messageArray = new byte[messageLength];

        for(int i = INIT; i < messageLength; i++)
            messageArray[i] = buffer.get();

        try {
            InetAddress address = InetAddress.getByAddress(messageArray);
            // Get the port number
            int port = buffer.getInt();
            this.mcastAddress = new InetSocketAddress(address,port);
        } catch (Exception e) { System.err.println("Unable to get address");}
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
        
        byte[] address = mcastAddress.getAddress().getAddress();
        
        // Place the contents of this data
        buffer.put((byte)address.length);
        buffer.put(address);
        buffer.putInt(mcastAddress.getPort());
        
        // Return the fully created message
        return message;
	}

    /**
     * Retrieve the address of the multicast group.
     */
    public InetSocketAddress getAddress() {
        return mcastAddress;
    }	
}
