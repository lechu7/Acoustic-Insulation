package pl.io;
import java.util.ArrayList;
import java.util.List;

public class Calculation {

	public Complex[] transformation(double[] list) {
    	Complex[] sinusSpectrum = FFT.fft(PreparingFFT.DataPreparingForFFT(list, PreparingFFT.RECTANGLE));
    	return sinusSpectrum;
	}

	public static  List<Double> diff(List<Double> listch1, List<Double> listch2) {
		List<Double> listdiff = new ArrayList<Double>();
		for(int i=0;i<listch1.size();i++)
			listdiff.add(listch1.get(i)-listch2.get(i));
		return listdiff;
	}
}