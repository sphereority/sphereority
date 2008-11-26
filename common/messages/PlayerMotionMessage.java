package common.messages;

import java.nio.ByteBuffer;
import common.Position;

/**
 * PlayerMotionMessage - Notifies that a player has killed another player.
 * @author rlagman
 */
public class PlayerMotionMessage extends Message implements MessageConstants {
	// SINGLETONS
	public static Logger logger = Logger.getLogger(CLIENT_LOGGER_NAME);

	protected Position position, velocity;
	protected float time;
	
    /**
     * Constructor - Creates a new PlayerMotionMessage.
     */
    public PlayerMotionMessage(byte playerId, 
                        float px, float py, float vx, float vy, float time) {
        super(MessageType.PlayerMotion, playerId, PlayerMotionLength);
        this.position = new Position(px,py);
        this.velocity = new Position(vx,vy);
        this.time  = time;
    }

    public PlayerMotionMessage(byte playerId, Position position, Position velocity, float time) {
        super(MessageType.PlayerMotion, playerId, PlayerMotionLength);
        this.position = position;
        this.velocity = velocity;
        this.time  = time;
    }

    /**
     * Constructor - Creates a new PlayerMotionMessage.
     * @param header Representation of a Header in bytes.
     * @param data Representation of the data portion in bytes.
     */
    public PlayerMotionMessage(byte[] header, byte[] data) {
        // Create the Message superclass
        super(header,PlayerMotionLength);
        
        // Wrap the stream of bytes into a buffer       
        ByteBuffer buffer = ByteBuffer.wrap(data);
		
		// Process the information to create the object.
        this.position = new Position(buffer.getFloat(),buffer.getFloat());
        this.velocity = new Position(buffer.getFloat(),buffer.getFloat());
        this.time  = buffer.getFloat();
    }
    
    /**
	 * Creates an array of bytes to be sent across the network
	 * that represents a Packetizable object.
	 * @return A byte representation of the Packetizable object.
	 */
	public byte[] getByteMessage() throws Exception {
        // Get the header
        byte[] header = getByteHeader();        
        byte[] message = new byte[header.length + dataLength];
        
        // Place the header information
        ByteBuffer buffer = ByteBuffer.wrap(message);
        buffer.put(header);
        
        // Place the contents of this data
        buffer.putFloat(position.getX());
        buffer.putFloat(position.getY());
        buffer.putFloat(velocity.getX());
        buffer.putFloat(velocity.getY());
        buffer.putFloat(time);
        
        // Return the fully created message
        return message;
	}
    
    /**
     * Retrieves the position of a player.
     * @return Position of a player.
     */
    public Position getPosition() {
		return position;
	}
	
    /**
     * Retrieves the velocity of a player.
     * @return Velocity of a player.
     */
	public Position getVelocity() {
        return velocity;
	}
	
	/**
	 * Retrives the time that this message was sent
	 * @return The time the player was at this point
	 */
	public float getTime()
	{
		return time;
	}
}
