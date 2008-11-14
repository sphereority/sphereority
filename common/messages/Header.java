package common.messages;

import java.nio.ByteBuffer;

/**
 *  Contains information common to all messages in Sphereority.
 *  @author Raphael Lagman
 */
public class Header {
    /**
     *  Constant - Maximum size of the header is 16 bytes.
     */
    public static final int HEADER_MAX = 8;

    /**
     *  message - Tells what kind of message is being associated with a packet.
     */
    protected MessageType message;

    /**
     *  Constructor - Initializes the parts of a Header.
     */
    protected Header(MessageType message) {
        this.message = message;
    }

    /**
     * Retrieves header information given an array of bytes.
     * @param header The header represented as bytes.
     * @return The Header object representation of the array of bytes.
     */
    public static Header getHeader(byte[] header) {
        ByteBuffer buffer = ByteBuffer.wrap(header);
        MessageType type = MessageAnalyzer.getMessageType(buffer.get());
        return new Header(type);
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

        // Return the header as a byte array
        return header;
    }

    /**
     *  Gets the message type associated with the header.
     */
    public MessageType getMessageType() {
        return message;
    }
}