package common.messages;

import java.nio.ByteBuffer;

/**
 * Encapsulates information common to all messages.
 * Also provides a way to figure out what kind of message is received.
 * 
 */
public abstract class Message {
    protected Header header;
    protected int dataLength;
    
    /**
     * Constructor - Creates a new message.
     * @param type The type of message
     */
    protected Message(MessageType type) {
        header = new Header(type);
    }
    
    /**
     * Constructs a message given the message in byte form.
     * @param message The message in byte form.
     * @return A Message object representing the message that was sent.
     */
    public static Message readMessage(byte[] message) {
        ByteBuffer buffer = ByteBuffer.wrap(message);
        
        // Store the header information
        byte[] byteHeader = new byte[Header.HEADER_MAX];
        buffer.get(byteHeader,0,8);
   
        // Store the data information
        int dataLength = message.length - Header.HEADER_MAX;
        byte[] byteData = new byte[dataLength];
        buffer.get(byteData,0,dataLength);       

        // Construct the header
        Header header = Header.getHeader(byteHeader);

        // Return a message with the specified header
        return MessageAnalyzer.getMessage(header.getMessageType(),byteData);
    }

    public abstract byte[] getByteMessage();

    /**
     * Helper method for sendMessage.  This creates the byte header for a
     * particular message.
     */
    protected byte[] getByteHeader() {
        return header.createHeader();
    }
}