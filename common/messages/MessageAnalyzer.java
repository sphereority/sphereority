package common.messages;

import java.nio.ByteBuffer;

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
	// SINGLETONS
	public static Logger logger = Logger.getLogger(CLIENT_LOGGER_NAME);

	public static final byte PLAYER_MOTION  = 0;
	public static final byte MAP_CHANGE     = 1;
	public static final byte SCORE_UPDATE   = 2;
	public static final byte HEALTH_UPDATE  = 3;
	public static final byte CHAT_MESSAGE   = 4;
	public static final byte DEATH_MESSAGE  = 5;
	public static final byte LOGIN_MESSAGE  = 6;
    public static final byte PLAYER_JOIN    = 7;
    public static final byte PLAYER_LEAVE   = 8;
	public static final byte MULTI_MESSAGE  = 9;
	public static final byte UNDEFINED      = -1;
	public static final int  INIT           = 0;
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
				message = MessageType.Chat;
				break;
			case DEATH_MESSAGE:
				message = MessageType.Death;
				break;
            case LOGIN_MESSAGE:
                message = MessageType.Login;
                break;
            case PLAYER_JOIN:
                message = MessageType.PlayerJoin;
                break;
            case PLAYER_LEAVE:
                message = MessageType.PlayerLeave;
                break;
            case MULTI_MESSAGE:
                message = MessageType.MulticastGroup;
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
			case Chat:
				message = CHAT_MESSAGE;
				break;
			case Death:
				message = DEATH_MESSAGE;
				break;
            case Login:
                message = LOGIN_MESSAGE;
                break;
            case PlayerJoin:
                message = PLAYER_JOIN;
                break;
            case PlayerLeave:
                message = PLAYER_LEAVE;
                break;
            case MulticastGroup:
                message = MULTI_MESSAGE;
                break;
			default:
				message = UNDEFINED;
		}
		return message;
    }

    /**
     * Retrieves a message given its header and the data portion of the message.
     * @return The message that the byte array represeneted.
     */
    public static Message getMessage(byte[] message) throws Exception {
        ByteBuffer buffer = ByteBuffer.wrap(message);
        return getMessage(buffer);
    }

    /**
     * Retrieves a message given its header and the data portion of the message.
     * @return The message that the byte array represeneted.
     */
    public static Message getMessage(ByteBuffer buffer) throws Exception {
        // Store the header information
        byte[] byteHeader = new byte[Header.HEADER_MAX];
        buffer.get(byteHeader,INIT,Header.HEADER_MAX);
   
        // Store the data information
        int dataLength = buffer.array().length - Header.HEADER_MAX;
        byte[] byteData = new byte[dataLength];
        buffer.get(byteData,INIT,dataLength);       

        // Construct the header
        Message receivedMessage = null;
		
        switch (getMessageType(byteHeader[MESSAGE_TYPE])) {
			case PlayerMotion:
                receivedMessage = new PlayerMotionMessage(byteHeader,byteData);
				break;
			case MapChange:
				break;
			case ScoreUpdate:
                receivedMessage = new ScoreUpdateMessage(byteHeader,byteData);
				break;
			case HealthUpdate:
                receivedMessage = new HealthUpdateMessage(byteHeader,byteData);
				break;
			case Chat:
                receivedMessage = new ChatMessage(byteHeader,byteData);
				break;
			case Death:
                receivedMessage = new DeathMessage(byteHeader,byteData);
				break;
            case Login:
                //receivedMessage = new LoginMessage(byteHeader,byteData);
                break;
            case MulticastGroup:
                receivedMessage = new MulticastGroupMessage(byteHeader,byteData);
                break;
            default:
            	System.out.println("Unknown packet.");
		}
		return receivedMessage;
    }
}
