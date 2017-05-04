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
	static final int RECORDING_TIME = 3000;
	
	/**
	 * @author Kamil Rytel
	 * 
	 * @return
	 * @throws Exception
	 */

	public static ArrayList<double[]> reading() throws Exception 
	{
		ArrayList<double[]> myList = new ArrayList<double[]>();
		
		try{
			//Declare audio format
	    	AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,44100,16,2,4,44100, false);
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
		   			double[][] expplodedArray = inputStream.explodeArray((int)format.getSampleRate(),format.getFrameSize(), targetLine);
		   			
		   			myList.add(expplodedArray[0]);
		   			myList.add(expplodedArray[1]);
		   		}		    
    		};
    		
    		//Save after some milliseconds and close line
    		thread.start();
    		Thread.sleep(inputStream.RECORDING_TIME+100);
    		
    		
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
	
		return myList;
	}
	
	/**
	 * @author Kamil Rytel
	 * @param sampleRate
	 * @param frameSize
	 * @param targetLine
	 * @return
	 */
	private static double[][] explodeArray(int sampleRate, int frameSize, TargetDataLine targetLine)
	{
			int bufferSize =  sampleRate * frameSize * inputStream.RECORDING_TIME/1000;		
			byte buffer[] = new byte[bufferSize];
			ByteArrayOutputStream out = new ByteArrayOutputStream();
	        out.write(buffer, 0,  targetLine.read(buffer, 0, buffer.length));
	        
			byte[] allSamplesToByte = out.toByteArray();
			double[] firstChanel = new double[bufferSize/2];
			double[] secondChanel = new double[bufferSize/2];	
			int helper = 0;
			int firstChanelCounter = 0;
			int secondChanelCounter = 0;
			
			//First 2 elements chanel one, next 2 chanel two
			for(int i=0; i < allSamplesToByte.length; i++){
				if(helper == 2) helper = -2;
				if(helper < 2 && helper > -1)
				{
					firstChanel[firstChanelCounter] = inputStream.convertByteToDouble(allSamplesToByte[i]);
					firstChanelCounter++;	
				}
				else{
					secondChanel[secondChanelCounter] =  inputStream.convertByteToDouble(allSamplesToByte[i]);
					secondChanelCounter++;
				}
				helper++;
			}
			
			double[][] result = new double[2][];
			result[0] = firstChanel;
			result[1] = secondChanel;
			
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