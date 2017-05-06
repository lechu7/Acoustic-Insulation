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
    	outputStream.sin(1000.0f, 3000);
		Thread.sleep(3500);
		outputStream.sweep(20, 20000, 2000);
		System.out.println("za sweep");
    	ArrayList<double[]> result = inputStream.reading();
    	double[] firstChannel = result.get(0);
    	double[] secondChannel = result.get(1);
    }
}
