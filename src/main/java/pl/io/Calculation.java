package pl.io;

public class Calculation {

	public static double[] calculateIsolation(double[] firstChannel, double[] secondChannel, double frequency){
		double[] dbs1 = calcDBs(firstChannel, frequency);
		double[] dbs2 = calcDBs(secondChannel, frequency);
		double[] difference = diff(dbs1, dbs2);
		
		return difference;
	}
	
	public static double[] calcDBs(double[] channel, double frequency){
		Complex[] fft = transformation(channel);
		double[] energySpectrum = PreparingFFT.EnergySpectrum(fft, channel.length);
		double[] dBs = dbs(energySpectrum, frequency, 1, fft.length);
		return dBs;
	}
	
	private static Complex[] transformation(double[] samples) {
    	Complex[] fft = FFT.fft(PreparingFFT.DataPreparingForFFT(samples));
    	return fft;
	}

	private static double[] diff(double[] listch1, double[] listch2) {
		double[] listdiff = new double[listch1.length];
		for(int i = 0; i < listch1.length; i++)
			listdiff[i] = listch1[i] - listch2[i];
		return listdiff;
	}
	
	private static double[] dbs(double[] PowerS, double fs, double P0, int N){
        int i;
        
        double f;
        double dB;
        
        double[] result = new double[(int)fs/2];
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