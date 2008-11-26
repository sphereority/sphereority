package common;

public interface MapChangeListener {
	// SINGLETONS
	public static Logger logger = Logger.getLogger(CLIENT_LOGGER_NAME);

	public void mapChanged(Map newMap);
}
