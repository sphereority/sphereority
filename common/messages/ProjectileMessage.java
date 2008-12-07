package common.messages;

import common.Constants;
import common.Position;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * ProjectileMessage - Notifies that a player has fired a projectile.
 * @author rlagman
 */
public class ProjectileMessage extends Message implements MessageConstants, Constants {
	public static Logger logger = Logger.getLogger(CLIENT_LOGGER_NAME);
    private Position startPos;
    private Position direction;

    
    /**
     * Constructor - Creates a new MulticastGroupMessage.
     * @param playerId The id of the player sending the message.
     */
    public ProjectileMessage(byte playerId, Position startPos, Position direction) {
        super(MessageType.Projectile, playerId, PlayerJoinLength);
        this.startPos = startPos;
        this.direction = direction;
    }

    /**
     * Constructor - Creates a new MulticastGroupMessage.
     * @param header Representation of a Header in bytes.
     * @param data Representation of the data portion in bytes.
     */
    public ProjectileMessage(byte[] header, byte[] data) {
        // Create the Message superclass
        super(header,PlayerJoinLength);
        
        // Wrap the stream of bytes into a buffer       
        ByteBuffer buffer = ByteBuffer.wrap(data);
        startPos  = new Position(buffer.getFloat(),buffer.getFloat());
		direction = new Position(buffer.getFloat(),buffer.getFloat());
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
        
        // Start position
        buffer.putFloat(startPos.getX());
        buffer.putFloat(startPos.getY());
        
        // Direction
        buffer.putFloat(direction.getX());
        buffer.putFloat(direction.getY());
        
        // Return the fully created message
        return message;
	}
    
    public Position getStartPosition() {
        return startPos;
    }
    
    public Position getDirection() {
        return direction;
    }
    

}
