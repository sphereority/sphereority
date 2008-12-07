package	common;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class describes a position set to or received from the network
 *
 */
public class PositionPacket {
	public static Logger logger = Logger.getLogger(CLIENT_LOGGER_NAME);

	protected Position position, velocity;
	protected float time;
	protected byte gameID, playerID;
	
	public PositionPacket(byte gameID, byte playerID, Position position, Position velocity, float time)
	{
		this.gameID = gameID;
		this.playerID = playerID;
		this.time = time;
		this.position = position;
		this.velocity = velocity;
	}
	
	public byte getGameID()
	{
		return gameID;
	}
	
	public byte getPlayerID()
	{
		return playerID;
	}
	
	public Position getPosition()
	{
		return position;
	}
	
	public Position getVelocity()
	{
		return velocity;
	}
}
