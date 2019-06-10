package mainpack;

import java.applet.AudioClip;

import org.jsfml.audio.Sound;
import org.jsfml.audio.SoundBuffer;
import org.jsfml.audio.SoundSource.Status;

public class MySound implements AudioClip{
	private Sound sound = new Sound();
	private boolean isMusic;
	
	public MySound(SoundBuffer buffer, boolean isMusic) {
		sound.setBuffer(buffer);
		this.isMusic = isMusic;
	}
	
	public void setVolume(float volume) {
		sound.setVolume(volume);
	}
	
	public boolean isMusic() {
		return isMusic;
	}
	
	public void pause() {
		if (sound != null)
			if (sound.getStatus() == Status.PLAYING)
				sound.pause();
	}
	
	@Override
	public void play() {
		if (sound != null){
			sound.setLoop(false);
			if (sound.getStatus() != Status.PLAYING)
				sound.play();
		}
	}

	@Override
	public void loop() {
		if (sound != null){
			sound.setLoop(true);
			if (sound.getStatus() != Status.PLAYING)
				sound.play();
		}
	}

	@Override
	public void stop() {
		if (sound != null)
			if (sound.getStatus() == Status.PLAYING)
				sound.stop();
	}
	
	public Status getStatus() {
		return sound.getStatus();
	}
}
