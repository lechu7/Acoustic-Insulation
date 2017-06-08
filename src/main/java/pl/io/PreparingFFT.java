package pl.io;

/*

    This is the special class for spectral analysis.
    
    This class needs other classes:
    
    Compex.java
    FFT.java
    
    (c)Piotr Wrzeciono, 2016
    
    @author Piotr Wrzeciono
    @since 2016.02.14
*/
public class PreparingFFT{
    
    /**
        This method is designed to increase the table dimension for the nearest power of 2.
        
        @param x This is the real input signal.
        @return The table with the extra zero elements.
        
    */
    public static double[] FillZeros(double[] x){
    
        double log2;
        int log2_int;
        int i;
        double[] result;
        int k;
        
        log2 = Math.log((double)x.length)/Math.log(2);
        log2_int = (int)Math.round(log2);
        
        result = null;
        
        k = (int)Math.pow(2.0,(double)log2_int);
        
        if(k < x.length) log2_int++; //in case when the k is too small
        
        k = (int)Math.pow(2.0,(double)log2_int);
        
        result = new double[k];
        
        for(i = 0; i < k; i++){
            if(i < x.length){
                result[i] = x[i];
            }else{
                result[i] = 0;
            }//end if
        }//next i
        
        return result;
        
    }//end of FillZeros
    
    
    /**
        The method which prepares the real input data for FFT calculation.
        
        This method does:
        1. The Window's correction.
        2. The zero filling for the nearest power of 2.
        3. The conversion form double[] to Complex[].
        
        @param x The real input signal without extra zeros.
        @param WindowT The window's type (RECTANGLE, BARTLETT, HANNING, HAMMING or BLACKMAN).
        
        @return The completly prepared signal for FFT (from the FFT class).
    */
    public static Complex[] DataPreparingForFFT(double[] x){
    
        Complex[] result;
        
        double[] data_for_calculation;
        double[] table_power_2;
        int i;
        
        data_for_calculation = x;
                
        table_power_2 = FillZeros(data_for_calculation);
        result = new Complex[table_power_2.length];
        
        for(i = 0; i < table_power_2.length; i++){
        
            result[i] = new Complex(table_power_2[i]/(double)table_power_2.length,0.0);
        }//next i
        
        return result;
    }//end of DataPreparingForFFT
    
    /**
        This function calculates the real frequency from the sample frequency, the index of the FFT sample and the original number of the input samples.
        
        @param n The index of FFT sample.
        @param fs The samle frequency.
        @param N The original number of elements in input data table (before the preparation for FFT calculation).
        
        @return The real frequency of FFT sample.
        
    */
    public static double FrequencyDenormalization(double n, double fs, double N){
    
        double result;
        
        result = n * fs/N;
        
        return result;
    }//end of FrequencyDenormalization
    
    
    /**
        The calculation of energy spectrum.
        
        @param X The FFT transform of input signal.
        @param original_length The original length of the input signal table.
        
        @return The main period (from the 0 to the N/2). It can be used to calculate the dB. Attention! The FFT or DFT of real signal is discrete and periodic!
    */
    public static double[] EnergySpectrum(Complex[] X, int original_length){
    
        double[] result;
        double fraction;
        int i;
        double value;
        
        result = new double[((int)(X.length/2)) + 1];
        
        fraction = (double)X.length/(double)original_length;
        
        for(i = 0; i < result.length; i++){
        
   
            value = X[i].abs() * X[i].abs() * fraction;
            if(i == 0){
                result[i] = value;
            }else{
                result[i] = 4 * value;
            }//end if
            
        }//next i
        
        return result;
        
    }//end of EnergySpectrum
    
    /**
        The calculation of decibel.
        
        @param P1 the power of signal.
        @param P0 the reference power.
        
        @return The dB value.
    */
    public static double Power_dB(double P1, double P0){
    
        double dB;
        
        dB = 10 * Math.log10(P1/P0);
        
        return dB;
        
    }//end of Power_dB
    
}
