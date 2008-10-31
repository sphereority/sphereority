package client.audio;

import java.io.*;

import javax.sound.sampled.*;

/**
 * This class maintains the list of sounds
 * @author Daryl Van Humbeck
 * I know, this is kind of lame, it only returns instances of SoundEffect and you could do that yourself, but there might a reason to do this later, so I'm being careful.
 */
public class GameSoundSystem
{
	protected Mixer mixer;
	
	public GameSoundSystem()
	{
		mixer = AudioSystem.getMixer(null);
	}
	
	public SoundEffect loadSoundEffect(String fileName)
	{
		try
		{
			return new SoundEffect(new File(fileName));
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}
		catch (UnsupportedAudioFileException e)
		{
			e.printStackTrace();
			return null;
		}
		catch (LineUnavailableException e)
		{
			e.printStackTrace();
			return null;
		}
	}
}
