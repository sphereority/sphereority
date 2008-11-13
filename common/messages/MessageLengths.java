package common.messages;

/**
 * Interface - Contains constants regarding the maximum length
 *             of messages.
 */
public interface MessageLengths {
	public final int PlayerMotionLength = 255,
                     MapChangeLength    = 255,
                     ScoreUpdateLength  = 255,
                     HealthUpdateLength = 255,
                     ChatMessageLength  = 255,
	                 DeathMessageLength = 255;
}
