package client.audio;

import java.io.*;
import javax.sound.sampled.*;

public class SoundEffect implements LineListener
{	
	protected float volume;
	protected File soundFile;
	protected Clip soundClip;
	protected Object lock;
	protected volatile boolean playing;
	protected FloatControl volumeControl;
	
	public SoundEffect(File fileName) throws IOException, UnsupportedAudioFileException, LineUnavailableException
	{
		soundFile = fileName;
		AudioInputStream in = AudioSystem.getAudioInputStream(soundFile);
		soundClip = AudioSystem.getClip();
		soundClip.open(in);
		soundClip.addLineListener(this);
		
		volume = 1.0f;
		lock = new Object();
		playing = false;
		
		if (soundClip.isControlSupported(FloatControl.Type.VOLUME))
			volumeControl = (FloatControl)soundClip.getControl(FloatControl.Type.VOLUME);
		else
		{
			System.out.println("Warning: No volume control available!");
			volumeControl = null;
		}
	}
	
	public void play()
	{
		playing = true;
		soundClip.setFramePosition(0);
		soundClip.start();
	}
	
	public void stop()
	{
		soundClip.stop();
		playing = false;
	}
	
	public void loop()
	{
		playing = true;
		soundClip.loop(Clip.LOOP_CONTINUOUSLY);
	}
	
	public void loop(int numTimes)
	{
		playing = true;
		soundClip.loop(numTimes);
	}

	public void update(LineEvent event)
	{
		System.out.printf("Event recieved from clip: %s\n", event.getType().toString());
		if (event.getType().equals(LineEvent.Type.STOP))
		{
			System.out.println("Stopped.");
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
	
	public float getVolume()
	{
		return volume;
	}
	
	public void setVolume(float v)
	{
		volume = v;
		if (volumeControl != null)
			volumeControl.setValue(volume);
	}
}
