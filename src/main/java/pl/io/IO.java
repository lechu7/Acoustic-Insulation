package pl.io;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * Class used for exporting data to .csv
 * 
 * @author Hubert Korgul
 *
 */
public class IO 
{
	/**
	 * Saves to .csv file information about signal - frequency, signal level for each of two channels and difference of signal for these two channels.
	 * 
	 * @param channel1 	sampled signal from first microphone
	 * @param channel2 	sampled signal from second microphone
	 * @param diff 		difference of channel1 and channel2
	 * @throws FileNotFoundException
	 */
	public static void saveCSV(double[] channel1, double[] channel2, double[] diff) throws FileNotFoundException{
	      PrintWriter pw = new PrintWriter("result.csv");
	      pw.println("Frequency;Front;Back;Difference");
	      
	      for (int i = 0; i < diff.length; i++) {
	    	  pw.println(i + ";" + channel1[i] +";" + channel2[i] + ";" + diff[i]);
	      }
	      System.out.println("File saved.");
	      pw.close();
	  }


}