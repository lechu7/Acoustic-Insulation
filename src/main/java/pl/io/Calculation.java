package pl.io;

public class Calculation {

	public static double[] calculateIsolation(double[] firstChannel, double[] secondChannel, double frequency){
		Complex[] fft1 = transformation(firstChannel);
		Complex[] fft2 = transformation(secondChannel);
		double[] energySpectrum1 = PreparingFFT.EnergySpectrum(fft1, firstChannel.length);
		double[] energySpectrum2 = PreparingFFT.EnergySpectrum(fft2, secondChannel.length);
		double[] energySpectrumDiff = diff(energySpectrum1, energySpectrum2);
		double[] dBs = dbs(energySpectrumDiff, frequency, 1, fft1.length);
		
		return dBs;		
	}
	
	public static double[] calcDBs(double[] channel, double frequency){
		Complex[] fft = transformation(channel);
		double[] energySpectrum = PreparingFFT.EnergySpectrum(fft, channel.length);
		double[] dBs = dbs(energySpectrum, frequency, 1, fft.length);
		return dBs;
	}
	
	public static Complex[] transformation(double[] samples) {
    	Complex[] fft = FFT.fft(PreparingFFT.DataPreparingForFFT(samples));
    	return fft;
	}

	public static double[] diff(double[] listch1, double[] listch2) {
		double[] listdiff = new double[listch1.length];
		for(int i = 0; i < listch1.length; i++)
			listdiff[i] = listch1[i] - listch2[i];
		return listdiff;
	}
	
	public static double[] dbs(double[] PowerS, double fs, double P0, int N){
        int i;
        
        double f;
        double dB;
        
        double[] result = new double[(int)fs];
        int a = 0;
        double sum = 0;
        int freq = 0;
        
        for(i = 0; i < PowerS.length; i++){
        
            f = PreparingFFT.FrequencyDenormalization((double)i, fs, (double)N);
            dB = PreparingFFT.Power_dB(PowerS[i],P0);
            
            a++;
            if ((int) f != freq){
            	result[freq] = sum / a;
            	freq = (int) f;
            	sum = dB;
            	a = 0;
            } else {
            	sum += dB;
            }
        }
        return result;
    }
	
}