package pl.io;

import java.util.ArrayList;

public class App 
{
	/**
	 * 
	 * @param args
	 * @throws Exception
	 */
    public static void main( String[] args ) throws Exception
    {
    	final float recordingTime = 3000;
		outputStream.sweep(20, 20000, recordingTime, 350);
    	ArrayList<double[]> result = inputStream.reading(recordingTime);
    	result.get(0);
    	result.get(1);
    }
}
