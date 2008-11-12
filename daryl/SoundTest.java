package daryl;

import client.audio.*;
import common.*;

public class SoundTest
{
	public static final int COUNT = 6;
	public static void main(String[] args)
	{
		GameSoundSystem gss = new GameSoundSystem();
		SoundEffect se = gss.loadSoundEffect(Constants.SOUND_BUMP);
		
		for (int i=1; i <= COUNT; i++)
		{
			se.setVolume((float)(COUNT-i)/COUNT);
			se.play();
			se.waitUntilDone();
			try { Thread.sleep(500); }
			catch (InterruptedException er) { }
		}
	}
}
