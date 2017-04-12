package pl.io;

import java.util.List;

public class Statistics {

	public double normalization(Object aList_without_outliners) {
		throw new UnsupportedOperationException();
	}

	public static  List<Double> outliners(List<Double> list) 
	{
		//obliczenie sredniej
		double average=0;
		for(int i=0;i<list.size();i++)
			average+=list.get(i);
		average=average/list.size();
		//obliczenie odchylenia standardowego
		double deviation=0;
		for(int i=0;i<list.size();i++)
			deviation += Math.pow(list.get(i)-average, 2);
		deviation = deviation/(list.size()-1);
		deviation = Math.sqrt(deviation);
		//obliczenie wasow
		double upper = average+deviation;
		double bottom = average-deviation;
		// sprawdzenie i zamiana
		for(int i=0;i<list.size();i++)
		{
			if(list.get(i)>upper) list.set(i, upper);
			if(list.get(i)<bottom) list.set(i, bottom);
		}
		return list;
	}
}