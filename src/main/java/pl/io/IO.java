package pl.io;

import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;
import javax.sound.sampled.*;


public class IO 
{
	final static String FILE_PATH = "sounds/";
	
	/**
	 * @author Kamil Rytel
	 * 
	 * @param targetLine
	 */
	public static void saveToFile(TargetDataLine targetLine) 
	{
		AudioInputStream audioStream = new AudioInputStream(targetLine);
		File audioFile = new File(IO.FILE_PATH+inputStream.FILE_NAME);
		
		try{
			//Write .wav file
			AudioSystem.write(audioStream, AudioFileFormat.Type.WAVE,audioFile);
		}catch(IOException e)
		{
			e.printStackTrace();
		}
	}


	public void saveCSV(List<Double[]> data) throws FileNotFoundException{
	      PrintWriter pw = new PrintWriter("file_X.csv");
	      pw.println("Frequency;Front;Back;Difference");
	      
	      for(Double[] elem : data){
	    	  pw.println(elem[0] + ";" + elem[1] +";" + elem[2] + ";" + elem[3]);
	      }
	      System.out.println("File saved.");
	      pw.close();
	  }

	public List<Double[]> readCSV(String path) throws FileNotFoundException{
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