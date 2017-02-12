package main;

import java.io.ByteArrayOutputStream;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

public class Record {

	private AudioFormat format;
	private DataLine.Info info;
	private ByteArrayOutputStream out;
	private byte[] byteArray;
	private float[] floatArray;

	public Record(){
			try {
				out = new ByteArrayOutputStream();
				format = new AudioFormat(AudioFormat.Encoding.PCM_UNSIGNED, 11025, 8, 1, 1, 1, false);
				info = new DataLine.Info(TargetDataLine.class, format);
				if(!AudioSystem.isLineSupported(info)){
					System.err.println("Line not supported!");
				}
				final TargetDataLine targetLine = (TargetDataLine)AudioSystem.getLine(info);
				targetLine.open();
				Thread targetThread = new Thread(){
					public void run(){
						targetLine.start();
						byte[] data = new byte[targetLine.getBufferSize()/5];
						int readBytes;
						System.out.println(targetLine.isActive());
						System.out.println(targetLine.isRunning());
						while(targetLine.isRunning()){
							readBytes = targetLine.read(data, 0, data.length);
							out.write(data, 0, readBytes);
						}
					}
				};
				targetThread.start();
				Thread.sleep(5000);
				targetLine.stop();
				targetLine.close();
				byteArray = out.toByteArray();
			} catch (LineUnavailableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	public byte[] getByteArray(){
		return byteArray;
	}

	public float[] getFloatArray(){
		floatArray = new float[byteArray.length];
		for(int i = 0; i<byteArray.length; i++){
			floatArray[i] = (float)byteArray[i];
		}
		return floatArray;
	}

	public AudioFormat getFormat(){
		return format;
	}
}
