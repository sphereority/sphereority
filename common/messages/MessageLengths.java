package common.messages;

/**
 * Interface - Contains constants regarding the maximum length
 *             of messages.
 */
public interface MessageLengths {
	public final int PlayerMotionLength = 25,
                     MapChangeLength    = 25,
                     ScoreUpdateLength  = 25,
                     HealthUpdateLength = 25,
                     ChatMessageLength  = 25,
	                 DeathMessageLength = 25;
}
