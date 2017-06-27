package pl.io;

/**
 * Class used for calculating transformation, isolation and difference in sound.
 */
public class Calculation {
	private static double[] calibrationChannel0, calibrationChannel1;
	private static boolean firstChannelIsBeforeBarrier = true;
	private static double minimalSignalStrength = 0;
	
	/**
	 * Calculates isolation in dBs for two specified channels.
	 * @param firstChannel	Sampled sound from first microphone
	 * @param secondChannel Sampled sound from second microphone
	 * @param frequency		Sampling frequency
	 * @return				Calculated sound isolation
	 */
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
	
	/**
	 * Calculates sound volume.
	 * @param channel	Sampled sound
	 * @param frequency	Sampling frequency
	 */
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
	/**
	 * Calculates difference of two arrays
	 * @param listch1	Minuend array
	 * @param listch2	Subtrahend array
	 */
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
	
	/**
	 * Sets calibration for channel0
	 * @param cal Calibration array
	 */
	public static void setCalibrationChannel0(double[] cal){
		calibrationChannel0 = cal;
	}
	
	/**
	 * Sets calibration for channel1
	 * @param cal Calbration array
	 */
	public static void setCalibrationChannel1(double[] cal){
		calibrationChannel1 = cal;
	}
	
	/**
	 * Setter method used to set which channel is placed before the barrier.
	 * @param b
	 */
	public static void setFirstChannelIsBeforeBarrier(boolean b){
		if (firstChannelIsBeforeBarrier != b){
			firstChannelIsBeforeBarrier = b;
		}
	}
	
	/**
	 * @return true - when first channel is placed before the barrier.
	 */
	public static boolean isFirstChannelBeforeBarrier(){
		return firstChannelIsBeforeBarrier;
	}
	
	/**
	 * Sets minimal required signal strength.
	 * @param min Value in percentages
	 */
	public static void setMinimalSignalStrength(double min){
		minimalSignalStrength = 32768 * (min / 100);
	}
	
	/**
	 * Checks if sampled signal passed as argument is considered strong enough.
	 * @param channel Sampled signal
	 * @return true - when signal is strong enough
	 */
	public static boolean isSignalStrongEnough(double[] channel){
		for (int i = 0; i < channel.length; i++){
			if (Math.abs(channel[i]) >= minimalSignalStrength) return true;
		}
		return false;
	}
	
}