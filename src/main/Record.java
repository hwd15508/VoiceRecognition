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
	private boolean stopped = false;

	public Record(){
			out = new ByteArrayOutputStream();
			format = new AudioFormat(AudioFormat.Encoding.PCM_UNSIGNED, 4000, 8, 1, 1, 4000, false);
			info = new DataLine.Info(TargetDataLine.class, format);
			if(!AudioSystem.isLineSupported(info)){
				System.err.println("Line not supported!");
			}
	}

	public void rec() {
		try {
			final TargetDataLine targetLine = (TargetDataLine) AudioSystem.getLine(info);
			targetLine.open(format);
			targetLine.start();
			Thread targetThread = new Thread() {
				public void run() {
					byte[] data = new byte[targetLine.getBufferSize()/5];
					int readBytes;
					while (!stopped) {
						readBytes = targetLine.read(data, 0, data.length);
						if (readBytes > 0) {
							out.write(data, 0, readBytes);
						}
					}
				}
			};
			targetThread.start();
			Thread.sleep(2000);
			targetLine.stop();
			targetLine.close();
			stopped = true;
			byteArray = out.toByteArray();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public byte[] getByteArray() {
		return byteArray;
	}

	public float[] getFloatArray() {
		floatArray = new float[byteArray.length];
		for (int i = 0; i < byteArray.length; i++) {
			floatArray[i] = (float) byteArray[i];
		}
		return floatArray;
	}

	public AudioFormat getFormat() {
		return format;
	}
}
