package common.messages;

import common.Constants;
import common.SpawnPoint;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * PlayerJoinMessage - Notifies that a player is joining the game.
 * @author rlagman
 */
public class PlayerJoinMessage extends Message implements MessageConstants, Constants {
	public static Logger logger = Logger.getLogger(CLIENT_LOGGER_NAME);

    private String playerName;
    private InetSocketAddress mcastAddress;
    private SpawnPoint sp;
    private long startTime=-1;
    
    /**
     * Constructor - Creates a new PlayerJoinMessage.
     * @param playerId The id of the player sending the message.
     * @param mcastAddress The address of the particular player/game.
     * @param sp The spawn point that the player is joining the map on.
     */
    public PlayerJoinMessage(byte playerId, String playerName,
                             InetSocketAddress mcastAddress,SpawnPoint sp) {
        this(playerId,playerName,mcastAddress,sp,false);
    }
    
    /**
     * Constructor - Creates a new PlayerJoinMessage.
     * @param playerId The id of the player sending the message.
     * @param mcastAddress The address of the particular player/game.
     * @param sp The spawn point that the player is joining the map on.
     * @param isAck Whether this message is acknowledging another packet.
     */
    public PlayerJoinMessage(byte playerId, String playerName,
                             InetSocketAddress mcastAddress,SpawnPoint sp,
                             boolean isAck) {
        super(MessageType.PlayerJoin, playerId, PlayerJoinLength, isAck);
        this.mcastAddress = mcastAddress;
        this.playerName = playerName;
        this.sp = sp;
    }

    /**
     * Constructor - Creates a new PlayerJoinMessage.
     * @param header Representation of a Header in bytes.
     * @param data Representation of the data portion in bytes.
     */
    public PlayerJoinMessage(byte[] header, byte[] data) {
        // Create the Message superclass
        super(header,PlayerJoinLength);
        
        // Wrap the stream of bytes into a buffer       
        ByteBuffer buffer = ByteBuffer.wrap(data);
		
        try {
            // Process the information to create the object.
            int messageLength = (int) buffer.get();
            byte[] messageArray = new byte[messageLength];

            for(int i = INIT; i < messageLength; i++)
                messageArray[i] = buffer.get();
            InetAddress address = InetAddress.getByAddress(messageArray);
            // Get the port number
            int port = buffer.getInt();
            this.mcastAddress = new InetSocketAddress(address,port);

            messageLength = (int) buffer.getInt();
            messageArray = new byte[messageLength];
            
            for(int i = INIT; i < messageLength; i++)
                messageArray[i] = buffer.get();
            // Recreate the player name
            playerName = new String(messageArray);
            // Get the spawn point
            sp = new SpawnPoint(buffer.getInt(),buffer.getInt());
            startTime = buffer.getLong();
        } catch (Exception e) { System.err.println("Unable to get address");}
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
        
        byte[] address = mcastAddress.getAddress().getAddress();
        
        // Place the contents of this data
        buffer.put((byte)address.length);
        buffer.put(address);
        buffer.putInt(mcastAddress.getPort());
        
        // Put the name
        buffer.putInt(playerName.length());
        buffer.put(playerName.getBytes());

        // Put the co-ordinates of the spawn point
        buffer.putInt(sp.getX());
        buffer.putInt(sp.getY());
        buffer.putLong(startTime);
        
        // Return the fully created message
        return message;
	}

    /**
     * Retrieve the address of the multicast group.
     * @return Get the multicast address for the player.
     */
    public InetSocketAddress getAddress() {
        return mcastAddress;
    }

    /**
     * Get the name of a player.
     * @return The name of the player.
     */
    public String getName() {
        return playerName;
    }	
    
    /**
     * Retrieves the Spawn Point of a player.
     * @return The spawn point for the player.
     */
    public SpawnPoint getSpawnPoint() {
        return sp;
    }
    
    /**
     * Sets the name of a player.
     * @param playerName The new name for the player.
     */
    public void setName(String playerName) {
        this.playerName = playerName;
    }
    
    /*
     * add a game start time to playerJoinMessage
     */
    public void setStartTime(long startTime){
    	this.startTime = startTime;
    }
    
    /**
     * Sets the address to be used.
     * @param address The new InetSocketAddress;
     */
    public void setAddress(InetSocketAddress address) {
        this.mcastAddress = address;
    }
}
