package common.messages;

/**

 *  MessageAnalyzer translates from the enumerated
 *  types of messages to their corresponding byte
 *  values.  As well, it constructs messages
 *  given an array of bytes that contain the whole
 *  message.

 *  @author Raphael Lagman 
 * 

 */
public abstract class MessageAnalyzer {
	
	public static final byte PLAYER_MOTION  = 0;
	public static final byte MAP_CHANGE     = 1;
	public static final byte SCORE_UPDATE   = 2;
	public static final byte HEALTH_UPDATE  = 3;
	public static final byte CHAT_MESSAGE   = 4;
	public static final byte DEATH_MESSAGE  = 5;
    public static final byte UNDEFINED      = -1;
    public static final int  MESSAGE_TYPE   = 0;	

	/**
	 * Translates from a byte to its corresponding
	 * message type value.
	 * @param type The byte representation of the type of message.
	 * @return The enumerated type representation of the type of message.
	 */
	public static MessageType getMessageType(byte type) {
		MessageType message;
		switch (type) {
			case PLAYER_MOTION:
				message = MessageType.PlayerMotion;
				break;
			case MAP_CHANGE:
				message = MessageType.MapChange;
				break;
			case SCORE_UPDATE:
				message = MessageType.ScoreUpdate;
				break;
			case HEALTH_UPDATE:
				message = MessageType.HealthUpdate;
				break;
			case CHAT_MESSAGE:
				message = MessageType.ChatMessage;
				break;
			case DEATH_MESSAGE:
				message = MessageType.DeathMessage;
				break;
			default:
				message = MessageType.Undefined;
		}
		return message;
	}

  /**
	 * Translates from a byte to its corresponding
	 * message type value.
	 * @param type The enumerated type representation of the type of message.
	 * @return The byte representation of the type of message.
	 */
    public static byte getMessageType(MessageType type) {
		byte message;
		switch (type) {
			case PlayerMotion:
				message = PLAYER_MOTION;
				break;
			case MapChange:
				message = MAP_CHANGE;
				break;
			case ScoreUpdate:
				message = SCORE_UPDATE;
				break;
			case HealthUpdate:
				message = HEALTH_UPDATE;
				break;
			case ChatMessage:
				message = CHAT_MESSAGE;
				break;
			case DeathMessage:
				message = DEATH_MESSAGE;
				break;
			default:
				message = UNDEFINED;
		}
		return message;
    }

    /**
     * Retrieves a message given its type and the data portion of the message.
     * @param type The type of message to create.
     * @param data The data portion of the message.
     * @return The message that the byte array represeneted.
     */
    public static Message getMessage(MessageType type, byte[] data) {
		Message message = null;
		
        switch (type) {
			case PlayerMotion:
				break;
			case MapChange:
				break;
			case ScoreUpdate:
				break;
			case HealthUpdate:
				break;
			case ChatMessage:
                message = ChatMessage.readByteMessage(data);
				break;
			case DeathMessage:
                message = DeathMessage.readByteMessage(data);
				break;
		}
		return message;
    }

}
