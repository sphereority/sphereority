package common.messages;

/**
 * Interface - Contains constants regarding to messages
 */
public interface MessageConstants {
    /**
     * Constant regarding to a particular message's data length.
     */
	public final int PlayerMotionLength = 25,
                     MapChangeLength    = 3000,
                     ScoreUpdateLength  = 25,
                     HealthUpdateLength = 25,
                     ChatMessageLength  = 258,
	                 DeathMessageLength = 2,
                     LoginMessageLength = 255,
                     MulticastGroupLength = 40;
    
    public final int MessageCap = 255;
    public final int INIT = 0;
    public final byte DefaultGameId = 0;
    public final String CharacterEncoding = "UTF-8";
}
