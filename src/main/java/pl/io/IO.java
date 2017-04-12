package pl.io;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.sound.sampled.*;

public class IO 
{
	final static String FILE_PATH = "sounds/";
	
	/**
	 * @author Kamil Rytel
	 * 
	 * @param targetLine
	 * @throws IOException 
	 * @throws UnsupportedAudioFileException 
	 */
	public static void saveToFile(TargetDataLine targetLine) 
	{
		AudioInputStream audioStream = new AudioInputStream(targetLine);
		File audioFile = new File(IO.FILE_PATH+inputStream.FILE_NAME);
		
		try{
			//Write .wav file
			AudioSystem.write(audioStream, AudioFileFormat.Type.WAVE, audioFile);
		}catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	public List<Integer> readFromFile(Object aString_path) {
		throw new UnsupportedOperationException();
	}

	public void saveCSV(Object aList) {
		throw new UnsupportedOperationException();
	}

	public List<Integer> readCSV(Object aString_path) {
		throw new UnsupportedOperationException();
	}
}