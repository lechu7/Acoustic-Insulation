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

    //There are constants, which represent different windows for signal analysis.

    /**The rectangle window*/
    public static final int RECTANGLE = 1;
    /**The Bartlett's window*/
    public static final int BARTLETT = 2;
    /**The Hanning's window*/
    public static final int HANNING = 3;
    /**The Hamming's window*/
    public static final int HAMMING = 4;
    /**The Blackman's window*/
    public static final int BLACKMAN = 5;
    
    
    /**
        The Bartlett's window implementation.
        
        @param x This is the real input signal.
        @return The signal multiplied by the Bartlett's window.
    */
    private static double[] BartlettWindow(double[] x){
    
        double[] result;
        int i;
        double N;
        double value;
        double n;
        
        result = new double[x.length];
        N = (double)x.length;
        
        for(i = 0; i < x.length; i++){
        
            value = x[i];
            n = (double)i;
            
            result[i] = (1.0-(Math.abs((n-((N-1.0)/2.0)))/(N-1.0))) * value;
        }//next i
        
        return result;
        
    }//end of Bartlett Window
    
    /**
        The Hanning's window implementation.
        
        @param x This is the real input signal.
        @return The signal multiplied by the Hanning's window.
    */
    private static double[] HanningWindow(double[] x){
    
        double[] result;
        int i;
        double N;
        double value;
        double n;
        
        result = new double[x.length];
        N = (double)x.length;
        
        for(i = 0; i < x.length; i++){
        
            value = x[i];
            n = (double)i;
            
            result[i] = (0.5*(1.0-Math.cos(2.0*Math.PI*n/(N-1.0)))) * value;
        }//next i
        
        return result;
        
    }//end of Hanning Window
    
    /**
        The Hamming's window implementation.
        
        @param x This is the real input signal.
        @return The signal multiplied by the Hamming's window.
    */
    private static double[] HammingWindow(double[] x){
    
        double[] result;
        int i;
        double N;
        double value;
        double n;
        
        result = new double[x.length];
        N = (double)x.length;
        
        for(i = 0; i < x.length; i++){
        
            value = x[i];
            n = (double)i;
            
            result[i] = (0.54-(0.46*Math.cos(2.0*Math.PI*n/(N-1.0)))) * value;
        }//next i
        
        return result;
        
    }//end of Hamming Window
    
    /**
        The Blackman's window implementation.
        
        @param x This is the real input signal.
        @return The signal multiplied by the Blackman's window.
    */
    private static double[] BlackmanWindow(double[] x){
    
        double[] result;
        int i;
        double N;
        double value;
        double n;
        
        double[] parts;
        
        parts = new double[2];
        result = new double[x.length];
        N = (double)x.length;
        
        for(i = 0; i < x.length; i++){
        
            value = x[i];
            n = (double)i;
            
            parts[0] = 0.42 - (0.50*Math.cos(2.0*Math.PI*n/(N-1.0)));
            parts[1] = 0.08*Math.cos(4*Math.PI*n/(N-1.0));
            
            result[i] = (parts[0] + parts[1]) * value;
        }//next i
        
        return result;
        
    }//end of Blackmann Window
    
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
        The calculating of signal energy.
        
        @param x The real input signal without extra zeros.
        @return Energy of the signal.
    */
    public static double Energy(double[] x){
    
        double result;
        int i;
        
        result = 0;
        
        for(i = 0; i < x.length; i++){
            result += (x[i] * x[i]);
        }//next i
        
        return result;
    }//end of Energy
    
    /**
        The calculating of signal power.
        
        @param x The real input signal without extra zeros.
        @return Power of the signal.
    */
    public static double Power(double[] x){
    
        double result;
        
        result = Energy(x)/((double)x.length);
        
        return result;
    }//end of Power
    
    
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
    public static Complex[] DataPreparingForFFT(double[] x, int WindowT){
    
        Complex[] result;
        
        double[] data_for_calculation;
        double[] table_power_2;
        int i;
        
        data_for_calculation = x;
        if(WindowT == BARTLETT) data_for_calculation = BartlettWindow(x);
        if(WindowT == HANNING) data_for_calculation = HanningWindow(x);
        if(WindowT == HAMMING) data_for_calculation = HammingWindow(x);
        if(WindowT == BLACKMAN) data_for_calculation = BlackmanWindow(x);
                
        table_power_2 = FillZeros(data_for_calculation);
        result = new Complex[table_power_2.length];
        
        for(i = 0; i < table_power_2.length; i++){
        
            result[i] = new Complex(table_power_2[i]/(double)table_power_2.length,0.0);
        }//next i
        
        return result;
    }//end of DataPreparingForFFT
    
    /**
        This method calculate the signal power from the FFT.
        
        @param X The FFT of the x signal,
        @param original_length The original length of input data table.
        
        @return The power of signal calculation by Parseval's theorem. This value has to be the same as the value obtained by the Power method.
    */
    public static double Parseval(Complex[] X, int original_length){
    
        double result;
        int i;
        double fraction;
        
        result = 0;
        
        for(i = 0; i < X.length; i++){
        
            result += (X[i].abs()*X[i].abs());
        }//next i
        
        fraction = (double)X.length/(double)original_length;
        
        result = result * fraction;
        
        return result;
        
    }//end of Parseval
    
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
