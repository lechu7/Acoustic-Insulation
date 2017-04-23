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

	public double[][] readFromFile(String path)
	{
		try
		{
			// Open the wav file specified as the first argument
			WavFile wavFile = WavFile.openWavFile(new File(path));
			
			// Display information about the wav file
			//wavFile.display();

			// Get the number of audio channels in the wav file
			int numChannels = wavFile.getNumChannels();
			
			// Create a buffer of n frames
			int frames = (int)wavFile.getNumFrames(); //total number of frames in wav file
			double[] buffer = new double[frames * numChannels];

			int framesRead;
			double min = Double.MAX_VALUE;
			double max = Double.MIN_VALUE;

			do
			{
				// Read frames into buffer
				framesRead = wavFile.readFrames(buffer, frames);
				
				// Loop through frames and look for minimum and maximum value
				for (int s=0 ; s<framesRead * numChannels ; s++)
				{
					if (buffer[s] > max) max = buffer[s];
					if (buffer[s] < min) min = buffer[s];
				}
			}
			while (framesRead != 0);
			
			// Separate channels
			double[][] channelsData = new double[numChannels][buffer.length/2];
			for(int i = 0, k = 0, t = 0; i<buffer.length; i++){
				if(i%2 == 0) {
					channelsData[0][k]=buffer[i]; k++;
				}
				else {
					channelsData[1][t]=buffer[i]; t++;
				}
			}
			
			// Close the wavFile
			wavFile.close();

			// Output the minimum and maximum value
			//System.out.printf("Min: %f, Max: %f\n", min, max);
			return channelsData;
		}
		catch (Exception e)
		{
			System.err.println(e);
		}
		return null;
	}

	public void saveCSV(List<Double[]> data) throws FileNotFoundException{
	      PrintWriter pw = new PrintWriter("file_X.csv");
	      pw.println("Frequency;Front;Back");
	      
	      for(Double[] elem : data){
	    	  pw.println(elem[0] + ";" + elem[1] +";" + elem[2]);
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
	      System.out.println(first[0] + ", "+ first[1] + ", " + first[2]);
	      Double[] second = new Double[]{
	    		  Double.parseDouble(first[0]), 
	    		  Double.parseDouble(first[1]), 
	    		  Double.parseDouble(first[2]),
	    		  };
	      
	      list.add(second);
	      }
	      
	      s.close();
	      return list;
	  }
}