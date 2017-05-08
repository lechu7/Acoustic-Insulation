package pl.io;
import java.util.ArrayList;
import java.util.List;

public class Calculation {

	public double transformation(Object aDouble_sample) {
		throw new UnsupportedOperationException();
	}

	public static  List<Double> diff(List<Double> listch1, List<Double> listch2) {
		List<Double> listdiff = new ArrayList<Double>();
		for(int i=0;i<listch1.size();i++)
			listdiff.add(listch1.get(i)-listch2.get(i));
		return listdiff;
	}
}