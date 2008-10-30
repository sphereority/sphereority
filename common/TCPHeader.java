package common;

import java.nio.ByteBuffer;

/**
 *  If a TCP packet comes in, use this class to analyze the
 *  application level data.
 *  @author Raphael Lagman
 */
public class TCPHeader extends Header{

    /**
     *  Constructor - Creates a new TCPHeader
     */
    public TCPHeader(MessageType message, int dataLength) {
        super(message,dataLength);        
    }
    
    /**
     *  Constructor - Creates an undefined TCPHeader
     */
    public TCPHeader() {
        super(MessageType.Undefined,-1);
    }

    /**
     * Creates a Header object from a set of bytes.
     * @param packet The packet that has been received.
     * @return The Java representation of the byte header.
     */
    public Header getHeader(byte[] packet) {
        // Wrap the packet into a buffer
        ByteBuffer buffer = ByteBuffer.wrap(packet);

        // Get the message type
        message = MessageAnalyzer.getMessage(buffer.get());
        // Get the data length
        dataLength = buffer.getInt();

        // Return the current instance of the TCPHeader
        return this;       
    }

    /**
     * Creates a byte representation of a Header.
     * @return A byte array that represents that structure of the header.
     */
    public byte[] createHeader() {
        // The byte array for the header
        byte[] header = new byte[HEADER_MAX];
        // Wrap the byte array into a buffer
        ByteBuffer buffer = ByteBuffer.wrap(header);
        // Put the total length of the message here (within one byte).
        buffer.put( (byte) (HEADER_MAX + dataLength));
        // Put the message type into the header
        buffer.put(MessageAnalyzer.getByteMessage(message));
        // Put the length of the data into the header
        buffer.putInt(dataLength);
        // Return the header
        return header;
    }
}