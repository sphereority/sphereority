package common;

import java.io.*;

public class ChatMessage
{
	public static final String MESSAGE_FORMAT = "%s: %s";
	
	protected String message;
	protected String sender;
	protected int senderID;
	
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
}
