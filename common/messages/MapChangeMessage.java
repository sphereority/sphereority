package common.messages;

import common.Map;
import common.Constants;
import java.nio.ByteBuffer;
import java.util.logging.Logger;

/**
 * MapChangeMessage - Notifies that the map is changing.
 * @author rlagman
 */
public class MapChangeMessage extends Message implements MessageConstants, Constants {
	public static Logger logger = Logger.getLogger(CLIENT_LOGGER_NAME);

    private Map map;
    
    /**
     * Constructor - Creates a new MapChangeMessage.
     * @param playerId The id of the player sending the message.
     */
    public MapChangeMessage(byte playerId, Map map) {
        super(MessageType.MapChange, playerId, MapChangeLength);
        this.map = map;
    }

    /**
     * Constructor - Creates a new MapChangeMessage.
     * @param header Representation of a Header in bytes.
     * @param data Representation of the data portion in bytes.
     */
    public MapChangeMessage(byte[] header, byte[] data) {
        // Create the Message superclass
        super(header,MapChangeLength);
        
        // Wrap the stream of bytes into a buffer       
        ByteBuffer buffer = ByteBuffer.wrap(data);
        
        // Get the name
        int nameLength = buffer.getInt();
        byte[] nameInBytes = new byte[nameLength];
        buffer.get(nameInBytes,0,nameLength);
        
        // Get the data
        int dataLength = buffer.getInt();
        byte[] dataInBytes = new byte[dataLength];
        buffer.get(dataInBytes,0,dataLength);
        
        map = new Map(new String(nameInBytes), new String(dataInBytes));
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
        
        buffer.putInt(map.getName().length());
        buffer.put(map.getName().getBytes(CharacterEncoding));
        buffer.putInt(map.getData().length());
        buffer.put(map.getData().getBytes(CharacterEncoding));

        // Return the fully created message
        return message;
	}	
}
