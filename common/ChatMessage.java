package common;

import java.io.*;
import java.nio.ByteBuffer;

public class ChatMessage implements Packetizable
{
	public static final String MESSAGE_FORMAT = "%s: %s";
	
	protected String message;
	protected String sender;
	protected int senderID;

    /**
     *  The header for the message.
     */
    private Header header;
	
	/**
	 * Create a ChatMessage with the specified contents
	 * @param message	The contents of the message to send
	 */
	public ChatMessage(String message)
	{
		this(null, -1, message);
	}
	
	/**
	 * Create a ChatMessage from a particular player
	 * @param sender		The username of the player who sent this message
	 * @param senderID		The id number of the player who sent this message
	 * @param message		The contents of the message
	 */
	public ChatMessage(String sender, int senderID, String message)
	{
		this.sender = sender;
		this.senderID = senderID;
		this.message = message;
	}
	
	/**
	 * Get the contents of the message
	 * @return	The message
	 */
	public String getMessage()
	{
		return message;
	}
	
	/**
	 * Get who sent the message
	 * @return	The username of the player who sent this message
	 */
	public String getSender()
	{
		return sender;
	}
	
	/**
	 * Get who sent the message
	 * @return	The player id of the player who sent this message
	 */
	public int getSenderID()
	{
		return senderID;
	}
	
	/**
	 * Send the message across the network via OutputStream
	 */
	public void sendMessage(OutputStream out) throws IOException
	{
		// TODO: Finish this when we find out what format the chat messages have
	}

    /**
     *  Constructor - Creates a new instance.
     */
    public ChatMessage() {
        header = new TCPHeader(MessageType.ChatMessage,255);
    }

	/**
	 * Reads the packet information and reconstructs
	 * the Packetizable object.
	 * @param data The raw byte data that was sent.
	 * @return The ChatMessage populated with the input data.
	 */
	public Packetizable readPacketData(byte[] data) {
        
        // Wrap the stream of bytes into a buffer
       
        ByteBuffer buffer = ByteBuffer.wrap(data);

		
        if(MessageAnalyzer.getMessage(buffer.get()) != MessageType.ChatMessage)
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
