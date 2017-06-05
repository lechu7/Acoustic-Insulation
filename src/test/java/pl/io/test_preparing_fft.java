public class test_preparing_fft {

    public static void main(String[] arg){
    
        double[] first = new double[]{1,2,3,4,5,3,2,1,0};
        double[] second;
        
        double PowerTime;
        double PowerFFT;
        Complex[] X;
        double[] sinus_table;
        Complex[] Sinus_spectrum;
        
        double[] EnergyS;
        
        // int[] WindowType;
        int i;
        
        second = PreparingFFT.FillZeros(first);
        
        System.out.println("The first table:");
        show_double_table(first);
        
        System.out.println();
        
        System.out.println("The second table:");
        show_double_table(second);
        
        // WindowType = new int[5];
        
        // WindowType[0] = PreparingFFT.RECTANGLE;
        // WindowType[1] = PreparingFFT.BARTLETT;
        // WindowType[2] = PreparingFFT.HANNING;
        // WindowType[3] = PreparingFFT.HAMMING;
        // WindowType[4] = PreparingFFT.BLACKMAN;
    
        X = FFT.fft(PreparingFFT.DataPreparingForFFT(first, PreparingFFT.RECTANGLE));
        PowerTime = PreparingFFT.Power(first);
        
        PowerFFT = PreparingFFT.Parseval(X,first.length);
        
        
        System.out.println("Power in the Time Domain = " + PowerTime + " Power in the Frequency Domain = " + PowerFFT);
        
        System.out.println();
        
        sinus_table = Sinus(2.0,10.0,30);
        
        System.out.println("Sinus table:");
        show_double_table(sinus_table);
        
        System.out.println();
        
        Sinus_spectrum = null;
        
        
        // for(i = 0; i < WindowType.length; i++){
            // Sinus_spectrum = FFT.fft(PreparingFFT.DataPreparingForFFT(sinus_table,WindowType[i]));
        
            // System.out.println("Sinus spectrum, window type nr " + i);
            // show_spectrum(Sinus_spectrum,10.0);
        
            // System.out.println();
        
        // }
        
        // System.out.println("\nEnergy spectrum:");
        
        EnergyS = PreparingFFT.EnergySpectrum(Sinus_spectrum,sinus_table.length);
        
        show_dB(EnergyS, 10, 1,Sinus_spectrum.length); //If the maximum value is 1, the power = 1.
        
    }//end of main
    
    
    public static double[] Sinus(double f, double fs, int N){
        
        double[] result;
        double t;
        int i;
        
        result = new double[N];
        
        
        for(i = 0; i < N; i++){
        
            t = ((double)i) * (1/fs);
        
            result[i] = Math.sin(2.0*Math.PI* f * t);
        
        }//next i
        
        return result;
        
    }//end of sinus generation;
    
    public static void show_double_table(double[] x){
    
        int i;
        
        for(i = 0; i < x.length; i++){
        
            System.out.println("x[" + i + "] = " + x[i]);
        }//next i
        
    }//end of show_double_table
    
    
    public static void show_spectrum(Complex[] X, double fs){
    
        int i;
        double freq;
        double amplitude;
        
        for(i = 0; i < X.length; i++){
        
            freq = PreparingFFT.FrequencyDenormalization((double)i, fs, (double)X.length);
            amplitude = X[i].abs();
            
            System.out.println(i + " " + freq + " " + amplitude);
        }//next i
    }//end of show_spectrum
    
    public static void show_dB(double[] PowerS, double fs, double P0, int N){
    
        int i;
        
        double f;
        double dB;
        
        for(i = 0; i < PowerS.length; i++){
        
            f = PreparingFFT.FrequencyDenormalization((double)i, fs, (double)N);
            dB = PreparingFFT.Power_dB(PowerS[i],P0);
            
            System.out.println(i + " " + f + " " + dB + " dB");
        }//next i
        
    }//end of show_dB
}
