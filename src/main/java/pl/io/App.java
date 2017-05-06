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
		outputStream.sweep(20, 20000, 2000);
    	ArrayList<double[]> result = inputStream.reading();
    	double[] firstChannel = result.get(0);
    	double[] secondChannel = result.get(1);
    }
}
