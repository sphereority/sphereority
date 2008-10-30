package common;

/**

 *  MessageAnalyzer

 *  @author Raphael Lagman 

 *.

 */
public abstract class MessageAnalyzer {
	
	public static final byte PLAYER_MOTION  = 0;
	public static final byte MAP_CHANGE     = 1;
	public static final byte SCORE_UPDATE   = 2;
	public static final byte HEALTH_UPDATE  = 3;
	public static final byte CHAT_MESSAGE   = 4;
	public static final byte DEATH_MESSAGE  = 5;
    public static final byte UNDEFINED      = -1;
	
	/**
	 * Translates from a byte to its corresponding
	 * message type value.
	 * @param type
	 * @return
	 */
	public static MessageType getMessage(byte type) {
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
	 * @param type
	 * @return
	 */
    public static byte getByteMessage(MessageType type) {
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
}
