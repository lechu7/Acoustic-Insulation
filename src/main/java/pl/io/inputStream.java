package pl.io;

import java.io.ByteArrayOutputStream;
import java.nio.ByteOrder;
import java.util.ArrayList;
import javax.sound.sampled.*;
import java.nio.ByteBuffer;

/**
 * @author Kamil Rytel
 */
public class inputStream 
{	
	/**
	 * @author Kamil Rytel
	 * 
	 * @return
	 * @throws Exception
	 */

	public static ArrayList<double[]> reading(final float recordingTime) throws Exception 
	{
		final ArrayList<double[]> myList = new ArrayList<double[]>();
		
		try{
			//Declare audio format
	    	final AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,44100,16,2,4,44100, false);
			DataLine.Info info = new DataLine.Info(TargetDataLine.class,format);
		    if(!AudioSystem.isLineSupported(info)){
		    	throw new Exception("Line not suported");
		    }
		    
		    final TargetDataLine targetLine = (TargetDataLine)AudioSystem.getLine(info);
		
		    //Start recording
		    targetLine.open(format);
		    targetLine.start();
		    
		    System.out.println("Start recording!");
		  
		    //Special thread, responsible for saving sound after some time-sleep method
		    Thread thread = new Thread()
    		{
		    	@Override public void run(){
		    		System.out.println(outputStream.getTime() + " Starting recording signal");
		   			double[][] expplodedArray = inputStream.explodeArray((int)format.getSampleRate(),format.getFrameSize(), targetLine, recordingTime);
		   			
		   			myList.add(expplodedArray[0]);
		   			myList.add(expplodedArray[1]);
		   		}		    
    		};
    		
    		//Save after some milliseconds and close line
    		thread.start();
    		Thread.sleep((long) (recordingTime+500));
    		
    		
    		targetLine.stop();
    		targetLine.close();
    		System.out.println(outputStream.getTime() + " Stop recording signal");
    		System.out.println("Recording finished!");
    	} 
    	catch(LineUnavailableException e){
    		e.printStackTrace();
    	}
    	catch(InterruptedException e){
    		e.printStackTrace();
    	}
	
		return myList;
	}
	
	/**
	 * @author Kamil Rytel
	 * @param sampleRate
	 * @param frameSize
	 * @param targetLine
	 * @return
	 */
	private static double[][] explodeArray(int sampleRate, int frameSize, TargetDataLine targetLine, float recordingTime)
	{
			int bufferSize =  (int) (sampleRate * frameSize * recordingTime/1000);		
			byte buffer[] = new byte[bufferSize];
			ByteArrayOutputStream out = new ByteArrayOutputStream();
	        out.write(buffer, 0,  targetLine.read(buffer, 0, buffer.length));
	        
			byte[] allSamplesToByte = out.toByteArray();
			double[] firstChannel = new double[bufferSize/2+1];
			double[] secondChannel = new double[bufferSize/2+1];	
			int helper = 0;
			int firstChannelCounter = 0;
			int secondChannelCounter = 0;
			
			//First 2 elements Channel one, next 2 Channel two
			for(int i=0; i < allSamplesToByte.length; i++){
				if(helper == 2) helper = -2;
				if(helper < 2 && helper > -1)
				{
					firstChannel[firstChannelCounter] = inputStream.convertByteToDouble(allSamplesToByte[i]);
					firstChannelCounter++;	
				}
				else{
					secondChannel[secondChannelCounter] =  inputStream.convertByteToDouble(allSamplesToByte[i]);
					secondChannelCounter++;
				}
				helper++;
			}
			
			double[][] result = new double[2][];
			result[0] = firstChannel;
			result[1] = secondChannel;
			
			return result;
	}
	
	/**
	 * @author Kamil Rytel
	 * 
	 * @param element
	 * @return
	 */
	private static double convertByteToDouble(byte element)
	{       
		return (double)ByteBuffer.wrap(inputStream.byteToByteArray(element)).order(ByteOrder.LITTLE_ENDIAN).getShort();	
	}
	
	/**
	 * @author Kamil Rytel
	 * 
	 * @param x
	 * @return
	 */
	private static byte[] byteToByteArray(byte x){
		byte[] wynik;
        short[] tablica_pom;
        int i;
        
        wynik = new byte[2];
        tablica_pom = new short[2];
        
        tablica_pom[0] = (short)(x & 0x00ff); 
        tablica_pom[1] = (short)((x & 0xff00) >> 8);
        
        
        for(i = 0; i < 2; i++) wynik[i] = (byte)tablica_pom[i];
        
        return wynik;
	}
}