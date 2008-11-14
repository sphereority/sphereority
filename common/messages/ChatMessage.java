package common.messages;

import java.nio.ByteBuffer;

/**
 * 
 * @author rlagman
 */
public class ChatMessage extends Message implements MessageLengths {
    /**
     * The id of the player who sent the message.
     */
    private byte playerId;

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
     */
    public ChatMessage(byte playerId, byte destId, String message) {
        super(MessageType.ChatMessage, ChatMessageLength);
        this.playerId         = playerId;
        this.destId           = destId;
    }

    public byte getPlayerId() {
        return playerId;
    }

    public byte getDestId() {
        return destId;
    }

    public String getMessage() {
        return message;    
    }

	/**
	 * Reads the packet information and reconstructs
	 * the Packetizable object.
	 * @param data The raw byte data that was sent.
	 * @return The ChatMessage populated with the input data.
	 */
	public static ChatMessage readByteMessage(byte[] data) {
        // Wrap the stream of bytes into a buffer       
        ByteBuffer buffer = ByteBuffer.wrap(data);
		
        
		// Process the information to create the object.
        byte playerId      = buffer.get();
        byte destId        = buffer.get();
        int messageLength = (int) buffer.get();
        
        char[] message = new char[messageLength];

        for(int i = 0; i < messageLength; i++)      
            message[i] = buffer.getChar();

		return new ChatMessage(playerId,destId,String.copyValueOf(message));
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
        buffer.put(playerId);
        buffer.put(destId);
        buffer.put((byte)this.message.length());
        try {
            buffer.put(this.message.getBytes("UTF-8"));
        } catch (Exception ex) { }
        
        // Return
        return message;
	}
}
