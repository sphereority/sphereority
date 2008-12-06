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
        this(type,playerId,dataLength,false);
    }
    
        /**
     * Constructor - Creates a new message.
     * @param type The type of message
     */
    protected Message(MessageType type, byte playerId, int dataLength,
                      boolean isAck) {
        header = new Header(type,DefaultGameId,playerId,isAck);
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
    
    /**
     * Retrieves the id of the game this message originated from.
     * @return The id of the game.
     */
    public byte getGameId() {
        return header.getGameId();
    }

    /**
     * Retrieves the message type of this message.
     * @return The message type.
     */
    public MessageType getMessageType() {
        return header.getMessageType();
    }
    
    /**
     * Specify the this message is acknowledging another one.
     */
    public boolean isAck() {
        return header.getIsAck();
    }
    
    /**
     * Specify whether a message is an ACK or not.
     */
    public void setAck(boolean isAck) {
        header.setAck(isAck);
    }
    
    /**
     * Set the playerId of the message.
     * @param playerId The new playerId
     */
    public void setPlayerId(byte playerId) {
        header.setPlayerId(playerId);
    }
}
