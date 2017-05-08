package pl.io;
import java.util.List;

public class Calculation {

	public double transformation(Object aDouble_sample) {
		throw new UnsupportedOperationException();
	}

	public static  List<Double> diff(List<Double> listch1, List<Double> listch2) {
		for(int i=0;i<listch1.size();i++)
			listch1.set(i,listch1.get(i)-listch2.get(i));
		return listch1;
	}
}