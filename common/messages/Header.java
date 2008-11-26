package common.messages;

import java.nio.ByteBuffer;

/**
 *  Contains information common to all messages in Sphereority.
 *  @author Raphael Lagman
 */
public class Header implements MessageConstants {
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
     */
    public Header(MessageType message, byte gameId, byte playerId) {
        this.message  = message;
        this.gameId   = gameId;
        this.playerId = playerId;
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
}