package mainpack;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Scanner;
import java.util.TreeMap;

import org.jsfml.audio.SoundBuffer;
import org.jsfml.audio.SoundSource.Status;

public class SoundManager {
	private static TreeMap<String,SoundBuffer> buffers = new TreeMap<String,SoundBuffer>();
	private static TreeMap<String,MySound> sounds = new TreeMap<String,MySound>();
	public static int musicVol, soundsVol;
	private static void addElement(String name, boolean isMusic) {
		SoundBuffer buffer = new SoundBuffer();
		try {
			buffer.loadFromFile(new File("sounds//" + name + ".wav").toPath());
			buffers.put(name, buffer);
			sounds.put(name, new MySound(buffer, isMusic));
		} catch (IOException e) {
			System.err.println("Error with sound loading! soundName = " + name);
		}
	}
	static {
		addElement("menuMusic", true);
		addElement("gameMusic", true);
		addElement("tankShot", false);
		addElement("tankStarts", false);
		addElement("tankStops", false);
		addElement("explosion", false);
		addElement("button", false);
		addElement("buttonHover", false);
		addElement("error", false);
		
		try {
			loadConfig("sounds//config.cfg");
		} catch (IOException e) {
			System.err.println("Error with sound config loading.");
		}
		setMusicVolume(musicVol);
		setSoundsVolume(soundsVol);
	}
	
	private SoundManager(){}
	
	
	public static void setSoundsVolume(float vol) {
		soundsVol = (int) vol;
		Iterator<String> set = sounds.navigableKeySet().iterator();
		while (set.hasNext()) {
			String string = (String) set.next();
			MySound tmp = sounds.get(string);
			if (tmp.isMusic() == false)
				tmp.setVolume(vol);
		}
	}
	public static void setMusicVolume(float vol) {
		musicVol = (int) vol;
		Iterator<String> set = sounds.navigableKeySet().iterator();
		while (set.hasNext()) {
			String string = (String) set.next();
			MySound tmp = sounds.get(string);
			if (tmp.isMusic() == true)
				tmp.setVolume(vol);
		}
	}
	public static int stop = 0;
	public static void playSound(String name) {
		sounds.get(name).play();
		if (name.equals("tankStarts")){
			stop = 10;
		}
	}
	public static void loopSound(String name) {
		sounds.get(name).loop();
	}
	public static void stopSound(String name) {
		if (name.equals("tankStarts") && stop > 0)
			return;
		sounds.get(name).stop();
	}
	public static void pauseSound(String name) {
		sounds.get(name).pause();
	}
	public static Status getStatus(String name) {
		return sounds.get(name).getStatus();
	}
	
	public static void saveConfig(String path) throws IOException {
		File file = new File(path);
		FileWriter fileWriter = new FileWriter(file);
		fileWriter.write(new Integer(musicVol) + "\n");
		fileWriter.write(new Integer(soundsVol) + "\n");
		fileWriter.close();
	}
	public static void loadConfig(String path) throws IOException {
		File file = new File(path);
		Scanner scanner = new Scanner(file);
		musicVol = scanner.nextInt();
		soundsVol = scanner.nextInt();
		scanner.close();
	}
}
