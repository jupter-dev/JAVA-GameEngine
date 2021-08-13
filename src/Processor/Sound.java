package Processor;

import java.io.*;
import javax.sound.sampled.*;

public class Sound {
		
	public static class Clips{
		public Clip[] clips;
		private int p;
		private int count;
		private float vol;
		
		public Clips(byte[] buffer, int count, float vol) throws LineUnavailableException, IOException, UnsupportedAudioFileException{
			if(buffer == null)
				return;
			
			clips = new Clip[count];
			this.count = count;
			this.vol = vol;
			
			for(int i = 0; i < count; i++) {
				clips[i] = AudioSystem.getClip();
				clips[i].open(AudioSystem.getAudioInputStream(new ByteArrayInputStream(buffer)));
			}
		}
		
		public void play() {
			if(clips == null) return;
			clips[p].stop();
			clips[p].setFramePosition(0);
			FloatControl gainControl = 
				    (FloatControl) clips[p].getControl(FloatControl.Type.MASTER_GAIN);
				gainControl.setValue(+vol);
			clips[p].start();
			p++;
			if(p>=count) p = 0;
		}
		
		public void loop() {
			if(clips == null) return;
			FloatControl gainControl = 
				    (FloatControl) clips[p].getControl(FloatControl.Type.MASTER_GAIN);
				gainControl.setValue(+vol);
			clips[p].loop(300);
			
		}
		
		public void stop() {
			if(clips == null) return;
			clips[p].stop();
			
		}
	}
	
	public static Clips EnemyDead  = load("/teste.wav", 1, -8.0f);

	private static Clips load(String name, int count, float vol) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DataInputStream dis = new DataInputStream(Sound.class.getResourceAsStream(name));
			
			byte[] buffer  = new byte[1024];
			int read = 0;
			while((read = dis.read(buffer)) >= 0) {
				baos.write(buffer, 0, read);
			}
			dis.close();
			byte[] data = baos.toByteArray();
			return new Clips(data,count, vol);
		}catch(Exception e) {
			try {
				return new Clips(null, 0, 0f);
			}catch(Exception ee) {
				return null;
			}
		}
	}

}
