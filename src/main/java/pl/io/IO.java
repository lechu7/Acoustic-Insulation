package pl.io;

import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;


public class IO 
{
	public static void saveCSV(double[] channel1, double[] channel2, double[] diff) throws FileNotFoundException{
	      PrintWriter pw = new PrintWriter("result.csv");
	      pw.println("Frequency;Front;Back;Difference");
	      
	      for (int i = 0; i < diff.length; i++) {
	    	  pw.println(i + ";" + channel1[i] +";" + channel2[i] + ";" + diff[i]);
	      }
	      System.out.println("File saved.");
	      pw.close();
	  }

	public static List<Double[]> readCSV(String path) throws FileNotFoundException{
	      File file = new File(path);
	      Scanner s = new Scanner(file);
	      List<Double[]> list = new ArrayList<Double[]>();
	      
	      s.nextLine();
	      
	      while(s.hasNextLine()){
	      String line = s.nextLine();
	      line = line.replace(',', '.');
	      String[] first = line.split(";");
	      
	      Double[] second = new Double[]{
	    		  Double.parseDouble(first[0]), //frequency value [Hz]
	    		  Double.parseDouble(first[1]), //sound pressure from 1st channel [dB]
	    		  Double.parseDouble(first[2]),	//sound pressure from 2nd channel [dB]
	    		  Double.parseDouble(first[3]),	//difference value [dB]
	    		  };
	      
	      list.add(second);
	      }
	      
	      s.close();
	      return list;
	  }
}