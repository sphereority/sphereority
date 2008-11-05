package	client;

import common.*;

/**
 * This class describes a human player who has local access to the keyboard and mouse
 * @author smaboshe
 *
 */
public class LocalPlayer extends Player {
	protected InputListener inputDevice;
	
	/**
	 * Due to the requirement that this type of Player needs information about
	 * key events and mouse motion, we <u>need</u> an object with information
	 * about the currently-pressed keys.
	 * @param input
	 */
	public LocalPlayer(InputListener input)
	{
		inputDevice = input;
	}
	
	public boolean animate(float dTime)
	{
		return false;
	}
}
