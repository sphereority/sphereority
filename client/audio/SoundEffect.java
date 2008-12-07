package client.audio;

import common.Constants;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.*;

/**
 * A handy sound-clip wrapper 
 * @author dvanhumb
 */
public class SoundEffect implements LineListener, Constants {	
	public static Logger logger = Logger.getLogger(CLIENT_LOGGER_NAME);

	protected float volume;
	protected File soundFile;
	protected Clip soundClip;
	protected Object lock = new Object();
	protected volatile boolean playing;
	protected FloatControl volumeControl;
	protected FloatControl gainControl;
	
	public SoundEffect(File fileName) throws IOException, UnsupportedAudioFileException, LineUnavailableException
	{
		soundFile = fileName;
		AudioInputStream in = AudioSystem.getAudioInputStream(soundFile);
		soundClip = AudioSystem.getClip();
		soundClip.open(in);
		soundClip.addLineListener(this);
		
		volume = 1.0f;
		playing = false;
		
		if (soundClip.isControlSupported(FloatControl.Type.VOLUME))
			volumeControl = (FloatControl)soundClip.getControl(FloatControl.Type.VOLUME);
		else
		{
			//System.out.println("Warning: No volume control available!");
			volumeControl = null;
		}
		if (soundClip.isControlSupported(FloatControl.Type.MASTER_GAIN))
			gainControl = (FloatControl)soundClip.getControl(FloatControl.Type.MASTER_GAIN);
		else
		{
			System.out.println("Warning: No gain control available!");
			gainControl = null;
		}
	}
	
	/**
	 * Start playing the sound, even if it's playing already
	 */
	public void play()
	{
		playing = true;
		soundClip.setFramePosition(0);
		soundClip.start();
	}
	
	/**
	 * Start playing the sound if it's not playing already
	 */
	public void playIfNot()
	{
		if (!playing)
			play();
	}
	
	/**
	 * Stop the sound playing
	 */
	public void stop()
	{
		soundClip.stop();
		playing = false;
	}
	
	/**
	 * Loop until we tell it to stop
	 */
	public void loop()
	{
		playing = true;
		soundClip.loop(Clip.LOOP_CONTINUOUSLY);
	}
	
	/**
	 * Loop for a certain number of times
	 * @param numTimes The number of times to loop
	 */
	public void loop(int numTimes)
	{
		playing = true;
		soundClip.loop(numTimes);
	}
	
	/**
	 * Returns true if this sound effect is currently being played
	 * @return	Whether the sound is being played or not
	 */
	public boolean isPlaying()
	{
		return playing;
	}
	
	public void update(LineEvent event)
	{
		if (event.getType().equals(LineEvent.Type.STOP))
		{
			playing = false;
			lock.notifyAll();
		}
	}
	
	public void waitUntilDone()
	{
		synchronized (lock)
		{
			while (playing)
			{
				try { lock.wait(10); }
				catch (InterruptedException er) { }
			}
		}
	}
	
	/**
	 * Get the current volume in the range of 0.0 to 1.0
	 * @return	The current volume
	 */
	public float getVolume()
	{
		return volume;
	}
	
	/**
	 * Set the current volume level
	 * @param v		The desired volume level between 0.0 and 1.0
	 * Note that if it might not actually change the volume level if the audio clip has no volume or gain controls avaiable
	 */
	public void setVolume(float v)
	{
		volume = v;
		if (volumeControl != null)
			volumeControl.setValue(volume);
		else if (gainControl != null)
		{
			float vol = -10*(1 - v);
			vol = Math.min(6, vol);
			gainControl.setValue(vol);
		}
	}
}
