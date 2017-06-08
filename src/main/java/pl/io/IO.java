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


}