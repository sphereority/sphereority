package daryl;

import client.audio.*;
import common.*;

public class SoundTest
{	
	public static void main(String[] args)
	{
		GameSoundSystem gss = new GameSoundSystem();
		SoundEffect se = gss.loadSoundEffect(Constants.SOUND_BUMP);
		
		for (int i=0; i < 5; i++)
		{
			se.setVolume((float)(4-i)/4);
			se.play();
			se.waitUntilDone();
			try { Thread.sleep(500); }
			catch (InterruptedException er) { }
		}
	}
}
