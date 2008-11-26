package common.messages;

/**
 * Enumerated Type - Specifies all the types of messages that exist
 *                   within Sphereority.
 */
public enum MessageType {
	// SINGLETONS
	public static Logger logger = Logger.getLogger(CLIENT_LOGGER_NAME);

	PlayerMotion,MapChange,ScoreUpdate,HealthUpdate,Chat,
	Death, Login, PlayerJoin, PlayerLeave, MulticastGroup, Undefined
}
