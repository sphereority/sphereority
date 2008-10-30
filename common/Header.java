package common;

/**
 *  Abstracts the basic properties of a header within the project.
 *  @author Raphael Lagman
 */
public abstract class Header {
    /**
     *  Constant - Maximum size of the header is 16 bytes.
     */
    protected final int HEADER_MAX = 8;

    /**
     *  message - Tells what kind of message is being associated with a packet.
     */
    protected MessageType message;

    /**
     *  dataLength - The length of the data portion of the packet.
     */
    protected int dataLength;

    /**
     *  Constructor - Initializes the parts of a Header.
     */
    protected Header(MessageType message, int dataLength) {
        this.message = message;
        this.dataLength = dataLength;
    }

    /**
     *  Creates the byte representation of the header.
     */
    protected abstract byte[] createHeader();

    /**
     *  Construct a header given an array of bytes.
     */
    protected abstract Header getHeader(byte[] packet);

    /**
     *  Gets the message type associated with the header.
     */
    public MessageType getMessageType() {
        return message;
    }

    /**
     * Gets the length of the data segment this header is
     * associated with.
     */
    public int getDataLength() {
        return dataLength;
    }
}