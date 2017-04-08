package pl.io;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;

import javax.sound.sampled.*;

public class outputStream{
	
	float SAMPLE_RATE=44100f;
	int SAMPLE_SIZE_IN_BITS = 16;
	int CHANNELS = 1;
	boolean SIGNED = true;
	boolean BIG_ENDIAN = true;
	byte audioData[] = new byte[(int)SAMPLE_RATE*32];
	
		
	ByteBuffer byteBuffer = ByteBuffer.wrap(audioData);
	ShortBuffer shortBuffer = byteBuffer.asShortBuffer();
	int byteLength = audioData.length;
	AudioInputStream audioInputStream;
	AudioFormat audioFormat;
	SourceDataLine sdl;
	
	
	void sweep() throws LineUnavailableException
	{
		
		InputStream byteArrayInputStream = new ByteArrayInputStream(audioData);
		audioFormat = new AudioFormat(SAMPLE_RATE, SAMPLE_SIZE_IN_BITS, CHANNELS, SIGNED, BIG_ENDIAN);
		audioInputStream = new AudioInputStream(byteArrayInputStream, audioFormat, audioData.length/audioFormat.getFrameSize());
		DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, audioFormat);
		sdl = (SourceDataLine)AudioSystem.getLine(dataLineInfo);

	    int bytesPerSamp = 2;
	    int sampLength = byteLength/bytesPerSamp;
	    double lowFreq = 20.0;
	    double highFreq = 20000.0;

	    for(int cnt = 0; cnt < sampLength; cnt++){
	      double time = cnt/SAMPLE_RATE;

	      double freq = lowFreq +
	               cnt*(highFreq-lowFreq)/sampLength;
	      double sinValue =
	                   Math.sin(2*Math.PI*freq*time);
	      shortBuffer.put((short)(16000*sinValue));
	    }
		Thread listenThread = new Thread()
		{
			@Override
			public void run()
			{
				try
				{		
					byte playBuffer[] = new byte[16384];

					
					sdl.open(audioFormat);
					sdl.start(); 			
					
					 int cnt; 
				     while((cnt = audioInputStream.read(playBuffer, 0, playBuffer.length)) != -1)
				      {
				        if(cnt > 0)
				        {			          
				          sdl.write(playBuffer, 0, cnt);
				        }
				      }
				      sdl.drain();
				      sdl.stop();
				      sdl.close();
					
				}
				catch(Exception e)
				{
					
				}
			}
		};
		listenThread.start();
	}

	
	public static void main(String[] args) {
		try
		{
			outputStream toTest = new outputStream();
			toTest.sweep();
		}
		catch(Exception e)
		{
			
		}
	}

}
