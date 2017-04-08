package pl.io;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;

/**
 * @author Kamil Rytel
 */
public class inputStream 
{
	static final String FILE_NAME = "file_name.wav";
	static final int RECORDING_TIME = 1000;
	
	/**
	 *
	 * @author Kamil Rytel
	 * 
	 * @return
	 * @throws Exception
	 */
	public static double reading() throws Exception 
	{	
		try{
			//Declare audio format
	    	AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,4410,16,2,4,4410, false);
			TargetDataLine line;
		
		    DataLine.Info info = new DataLine.Info(TargetDataLine.class,format);
		    if(!AudioSystem.isLineSupported(info)){
		    	throw new Exception("Line not suported");
		    }
		    
		    TargetDataLine targetLine = (TargetDataLine)AudioSystem.getLine(info);
		    
		    //Start recording
		    targetLine.open(format);
		    targetLine.start();
		    System.out.println("Start recording!");
		    
		    //Special thread, responsible for saving sound after some time-sleep method
		    Thread thread = new Thread()
    		{
		    	@Override public void run(){
		    		IO.saveToFile(targetLine);
		    	}
    		};
    		
    		//Save after some milliseconds and close line
    		thread.start();
    		Thread.sleep(inputStream.RECORDING_TIME);
    		targetLine.stop();
    		targetLine.close();
    		System.out.println("Recording finished!");
    	} 
    	catch(LineUnavailableException e){
    		e.printStackTrace();
    	}
    	catch(InterruptedException e){
    		e.printStackTrace();
    	}
		
		return 0;
	}
}