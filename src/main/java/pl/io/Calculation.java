package pl.io;

public class Calculation {
	private static double[] calibrationChannel0, calibrationChannel1;
	private static boolean firstChannelIsBeforeBarrier = true;
	private static double minimalSignalStrength = 0;
	
	public static double[] calculateIsolation(double[] firstChannel, double[] secondChannel, double frequency){
		double[] dbs1 = calcDBs(firstChannel, frequency);
		double[] dbs2 = calcDBs(secondChannel, frequency);
		
		if (calibrationChannel0 != null && calibrationChannel1 != null){
			if (firstChannelIsBeforeBarrier){
				dbs1 = diff(dbs1, calibrationChannel0);
				dbs2 = diff(dbs2, calibrationChannel1);
			} else {
				dbs1 = diff(dbs1, calibrationChannel1);
				dbs2 = diff(dbs2, calibrationChannel0);
			}
		}
		
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

	public static double[] diff(double[] listch1, double[] listch2) {
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
	
	public static void setCalibrationChannel0(double[] cal){
		calibrationChannel0 = cal;
	}
	
	public static void setCalibrationChannel1(double[] cal){
		calibrationChannel1 = cal;
	}
	
	public static void setFirstChannelIsBeforeBarrier(boolean b){
		if (firstChannelIsBeforeBarrier != b){
			firstChannelIsBeforeBarrier = b;
		}
	}
	
	public static boolean isFirstChannelBeforeBarrier(){
		return firstChannelIsBeforeBarrier;
	}
	
	public static void setMinimalSignalStrength(double min){
		minimalSignalStrength = 32768 * (min / 100);
	}
	
	public static boolean isSignalStrongEnough(double[] channel){
		for (int i = 0; i < channel.length; i++){
			if (Math.abs(channel[i]) >= minimalSignalStrength) return true;
		}
		return false;
	}
	
}