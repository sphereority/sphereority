package common.messages;

import common.Constants;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *  Contains information common to all messages in Sphereority.
 *  @author Raphael Lagman
 */
public class Header implements MessageConstants, Constants {
	// SINGLETONS
	public static Logger logger = Logger.getLogger(CLIENT_LOGGER_NAME);

    /**
     *  Constant - Maximum size of the header is 16 bytes.
     */
    public static final int HEADER_MAX = 8;

    /**
     *  message - Tells what kind of message is being associated with a packet.
     */
    protected MessageType message;

    /**
     * isAck - Used for reliable communications to confirm whether this
     *         is an acknowledgment.
     */
    protected boolean isAck;
    
    /**
     * gameId - Identifies a game that is being played.
     */
    protected byte gameId;
    
    /**
     * playerId - Identifies a player in the game.
     */
    protected byte playerId;
    
    /**
     *  Constructor - Initializes the parts of a Header.
     *  @param message The type of message the header is associated with
     *  @param gameId The id of the game (unused at the moment)
     *  @param playerId The id of the player sending a message.
     *  @param isAck Whether this message is an acknowledgement or not.
     */
    public Header(MessageType message, byte gameId, byte playerId, boolean isAck) {
        this.message  = message;
        this.gameId   = gameId;
        this.playerId = playerId;
        this.isAck    = isAck;
    }

        /**
     *  Constructor - Initializes the parts of a Header.
     *  @param message The type of message the header is associated with
     *  @param gameId The id of the game (unused at the moment)
     *  @param playerId The id of the player sending a message.
     *  @param isAck Whether this message is an acknowledgement or not.
     */
    public Header(MessageType message, byte gameId, byte playerId) {
        this(message,gameId,playerId,false);
    }
    
    /**
     * Constructor - Creates a header given a byte representation of an
     *               instance of a header.
     * @param header The header represented as bytes.
     */
    public Header(byte[] header) {
        ByteBuffer buffer = ByteBuffer.wrap(header);
        this.message  = MessageAnalyzer.getMessageType(buffer.get());
        this.gameId   = buffer.get();
        this.playerId = buffer.get();  
        this.isAck    = buffer.get() == 1;
    }
    
    /**
     * Creates a byte representation of a header.
     * @return The header as a byte array.
     */
    public byte[] createHeader() {
        // Create the header and a buffer to wrap it
        byte[] header = new byte[HEADER_MAX];
        ByteBuffer buffer = ByteBuffer.wrap(header);

        // Put in the contents of a header
        buffer.put(MessageAnalyzer.getMessageType(message));
        buffer.put(gameId);
        buffer.put(playerId);
        buffer.put(isAck ? (byte)1 : (byte)0);
        
        // Return the header as a byte array
        return header;
    }

    /**
     *  Gets the message type associated with the header.
     */
    public MessageType getMessageType() {
        return message;
    }
    
    /**
     *  Gets the player associated with this message.
     */
    public byte getPlayerId() {
        return playerId;
    }
    
    /**
     * Gets the gameId associated with this message.
     */
    public byte getGameId() {
        return gameId;
    }
    
    public boolean getIsAck() {
        return isAck;
    }
    
    public void setPlayerId(byte playerId) {
        this.playerId = playerId;
    }
    
    public void setAck(boolean isAck) {
        this.isAck = isAck;
    }
}