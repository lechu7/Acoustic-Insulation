package pl.io;

import java.io.FileNotFoundException;
import java.io.PrintWriter;


public class IO 
{
	/**
	 * Zapisuje do pliku .csv dane sygnału - częstotliwość, poziom natężenia dźwięku dla dwóch kanałów kanałów i różnicę tych poziomów
	 * 
	 * @param channel1
	 * @param channel2
	 * @param diff
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