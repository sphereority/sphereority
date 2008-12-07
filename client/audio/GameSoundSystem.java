package client.audio;

import common.Constants;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.*;

/**
 * This class maintains the list of sounds
 * @author Daryl Van Humbeck
 * I know, this is kind of lame, it only returns instances of SoundEffect and you could do that yourself, but there might a reason to do this later, so I'm being careful.
 */
public class GameSoundSystem implements Constants {
	public static Logger logger = Logger.getLogger(CLIENT_LOGGER_NAME);

	protected Mixer mixer;
	
	public GameSoundSystem()
	{
		mixer = AudioSystem.getMixer(null);
	}
	
	/**
	 * Returns a sound effect given the name of the sound file.
	 * @param fileName	The name of the sound file
	 * @return		The SoundEffect if the file was loaded successfully, or null if it failed
	 */
	public SoundEffect loadSoundEffect(String fileName)
	{
		try
		{
			return new SoundEffect(new File(fileName));
		}
		catch (UnsupportedAudioFileException e)
		{
			System.err.printf("client.audio.GameSoundSyste: Error: File '%s' is an unsupported type!\n", fileName);
			return null;
		}
		catch (LineUnavailableException e)
		{
			System.err.printf("client.audio.GameSoundSyste: Error: Can't find an audio line to play the sound on!\n");
			return null;
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}
	}
}
