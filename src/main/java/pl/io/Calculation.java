package pl.io;
import java.util.ArrayList;
import java.util.List;

public class Calculation {

	public static double[] calculateIsolation(double[] firstChannel, double[] secondChannel, double frequency){
		Complex[] fft1 = transformation(firstChannel);
		Complex[] fft2 = transformation(secondChannel);
//		Complex[] difference = diff(fft1, fft2);
		double[] energySpectrum1 = PreparingFFT.EnergySpectrum(fft1, firstChannel.length);
		double[] energySpectrum2 = PreparingFFT.EnergySpectrum(fft2, secondChannel.length);
		double[] energySpectrumDifference = diff(energySpectrum1, energySpectrum2);
		double[] dBs = dbs(energySpectrumDifference, frequency, -99, fft1.length);
		
		return dBs;
//		double[] spectrum1 = spectrumFromFFT(fft1, frequency);
//		double[] spectrum2 = spectrumFromFFT(fft2, frequency);
//		double[] difference = diff(spectrum1, spectrum2);			//TODO Uwzględnić różnicę w kalibracji
		
	}
	
	public static Complex[] transformation(double[] samples) {
    	Complex[] fft = FFT.fft(PreparingFFT.DataPreparingForFFT(samples));
    	return fft;
	}
	
//	public static double[] spectrumFromFFT(Complex[] fft, double frequency){
//    	double[] spectrum = new double[(int) frequency];
//    	int freqSum = 0;
//    	double amplitudeSum = 0;
//    	for(int i = 0; i < fft.length; i += 1){
//         	double freq = PreparingFFT.FrequencyDenormalization((double)i, 48000, (double)fft.length);
//        	double amplitude = fft[i].abs();
//        	
//        	if ((int) freq != freqSum){
//        		spectrum[freqSum] = amplitudeSum;
//        		amplitudeSum = amplitude;
//        		freqSum = (int) freq;
//        	} else {
//        		amplitudeSum += amplitude;
//        	}
//    	}
//        return spectrum;
//	}

	public static double[] diff(double[] listch1, double[] listch2) {
		double[] listdiff = new double[listch1.length];
		for(int i = 0; i < listch1.length; i++)
			listdiff[i] = listch1[i] - listch2[i];
		return listdiff;
	}
	
//	public static Complex[] diff(Complex[] fft1, Complex[] fft2){
//		Complex[] difference = new Complex[fft1.length];
//		for (int i = 0; i < difference.length; i++){
//			difference[i] = fft1[i].minus(fft2[i]);
//		}
//		return difference;
//	}
	
	public static double[] dbs(double[] PowerS, double fs, double P0, int N){
        int i;
        
        double f;
        double dB;
        
        for(i = 0; i < PowerS.length; i++){
        
            f = PreparingFFT.FrequencyDenormalization((double)i, fs, (double)N);
            dB = PreparingFFT.Power_dB(PowerS[i],P0);
            
            System.out.println(i + " " + f + " " + dB + " dB");
        }
        return null;
    }
}