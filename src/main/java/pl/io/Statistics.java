package pl.io;

import java.util.List;

public class Statistics {

	public static List<Double> normalization(List<Double> list) 
	{
		double max = list.get(0);
		double min = list.get(0);
		//Obliczenie wartosci najmniejszej i najwiekszej
		for (int i = 1; i < list.size(); i++) {
			if (list.get(i)>max) max = list.get(i);
			if (list.get(i)<min) min = list.get(i);
		}
		//normalizacja
		for(int i=0;i<list.size();i++)
			list.set(i, (list.get(i)-min)/(max-min)*2-1); 
		return list;
	}

	public List outliners(Object aList_from_file) {
		throw new UnsupportedOperationException();
	}
}