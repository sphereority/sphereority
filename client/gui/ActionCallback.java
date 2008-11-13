package client.gui;

public interface ActionCallback
{
	/**
	 * This gets called when a widget sends a notification
	 * @param source	The object that originated this event
	 * @param buttons	The mouse buttons pressed on this event
	 */
	public void actionCallback(InteractiveWidget source, int buttons);
}
