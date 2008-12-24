package common.messages;

import common.Constants;
import java.nio.ByteBuffer;
import java.util.logging.Logger;

/**
 * ScoreUpdateMessage - Notifies that a player's score has changed.
 * @author rlagman
 */
public class ScoreUpdateMessage extends Message implements MessageConstants, Constants {
	public static Logger logger = Logger.getLogger(CLIENT_LOGGER_NAME);

    private int newScore;
    
    /**
     * Constructor - Creates a new ScoreUpdateMessage.
     * @param playerId The id of the player sending the message.
     * @param newScore The new score for the player
     */
    public ScoreUpdateMessage(byte playerId, int newScore) {
        super(MessageType.ScoreUpdate, playerId, ScoreUpdateLength);
        this.newScore = newScore;
    }

    /**
     * Constructor - Creates a new ScoreUpdateMessage.
     * @param header Representation of a Header in bytes.
     * @param data Representation of the data portion in bytes.
     */
    public ScoreUpdateMessage(byte[] header, byte[] data) {
        // Create the Message superclass
        super(header,ScoreUpdateLength);
        
        // Wrap the stream of bytes into a buffer       
        ByteBuffer buffer = ByteBuffer.wrap(data);
		
		// Process the information to create the object.
        this.newScore = buffer.getInt();
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
        
        // Place the contents of this data
        buffer.putInt(newScore);
       
        // Return the fully created message
        return message;
	}	
}
