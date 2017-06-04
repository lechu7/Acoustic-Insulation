package pl.io;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
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
    	
    	InputStream inputStream = new InputStream();
        double[][] inputStreamResponse = inputStream.reading(recordingTime);
        
    	double[] firstChannel = inputStreamResponse[0];
    	double[] secondChannel = inputStreamResponse[1];
    	for (int i = 0; i < firstChannel.length; i++){
    		System.out.println(firstChannel[i]);
    	}
    	
    	int zerosCh1 = 0;
    	int zerosCh2 = 0;
    	for (int i = 2; i < firstChannel.length - 2; i += 3){
    		if (Math.signum(firstChannel[i - 2]) != Math.signum(firstChannel[i])){
    			zerosCh1++;
    		}
    		if (Math.signum(secondChannel[i - 2]) != Math.signum(secondChannel[i])){
    			zerosCh2++;
    		}
    	}
    	System.out.println("Liczba zer kanal pierwszy: " + zerosCh1);
    	System.out.println("Liczba zer kanal drugi: " + zerosCh2);

    	// Tylko pierwszy kanał
    	Complex[] sinusSpectrum = FFT.fft(PreparingFFT.DataPreparingForFFT(firstChannel, PreparingFFT.RECTANGLE));
    	save_spectrum(sinusSpectrum, 48000);
    }
    
    public static void save_spectrum(Complex[] X, double fs) throws FileNotFoundException{
        
        int i;
        double freq;
        double amplitude;
        PrintWriter out = new PrintWriter("spectrum.txt");

        for(i = 0; i < X.length; i += 1){
        	freq = PreparingFFT.FrequencyDenormalization((double)i, fs, (double)X.length);
        	amplitude = X[i].abs();
        	if (freq> 20000) break;
            
//            System.out.println(i + " \t " + freq / n + " \t " + amplitude);
        	out.println(freq + " " + amplitude);
        }
        out.close();
    }
}
