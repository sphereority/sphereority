package common.messages;

/**
 * Encapsulates information common to all messages.
 * Also provides a way to figure out what kind of message is received.
 */
public abstract class Message implements MessageConstants {
    protected Header header;
    protected int dataLength;
    
    /**
     * Constructor - Creates a new message.
     * @param type The type of message
     */
    protected Message(MessageType type, byte playerId, int dataLength) {
        header = new Header(type,DefaultGameId,playerId);
        this.dataLength = dataLength;
    }
    
    /**
     * Constructor - Creates a new message.
     * @param type The type of message
     */
    protected Message(byte[] header, int dataLength) {
        this.header = new Header(header);
        this.dataLength = dataLength;
    }
    
    /**
     * A class extending Message must be able to create a byte
     * representation of itself.  As a result, it must implement this
     * method.
     */
    public abstract byte[] getByteMessage() throws Exception;

    /**
     * Helper method for getByteMessage().  This creates the byte header for a
     * particular message.
     */
    protected byte[] getByteHeader() {
        return header.createHeader();
    }
    
    /**
     * Retrieves the id of the player who sent this message.
     * @return The id of a player.
     */
    public byte getPlayerId() {
        return header.getPlayerId();
    }
    
    public byte getGameId() {
        return header.getGameId();
    }
}