package pl.io;

public class Statistics {
	 /**
	  *The method takes as a array parameter and returns a new normalized
	  list with values from -1 to 1.
	  * @param tab
	  */
    public static double[] normalization(double[] tab)
    {
        double max = tab[0];                        //Defining and calculating the max value
        double min = tab[0];                        //Defining and calculating the min value
        for (int i = 1; i < tab.length; i++) {
            if (tab[i]>max) max = tab[i];   //If the tab item is greater than the temporary maximum, temporary becomes of this value
            if (tab[i]<min) min = tab[i];       //If the tab item is lower than the temporary minimum, temporary becomes of this value
        }
       
        for(int i=0;i<tab.length;i++)                   //Normalization
            tab[i] = (tab[i]-min)/(max-min)*2-1;
           
        return tab;                                 //Return a normalized tab
    }
 
    /**
	 *The method takes an array as a parameter, calculates outliners, 
	 then searches for values in excess and converts them to calculated outliners.
     * @param tab
     */
    public static  double[] outliners(double[] tab)
    {
       
        double average=0;                           //Defining and calculating of average value
        for(int i=0;i<tab.length;i++)
            average+=tab[i];
        average=average/tab.length;
       
        double deviation=0;                         //Calculation of standard deviation
        for(int i=0;i<tab.length;i++)
            deviation += Math.pow(tab[i]-average, 2);
        deviation = deviation/(tab.length-1);
        deviation = Math.sqrt(deviation);
                                                    //Calculation of outliners
        double upper = average+2*deviation;         //Defining and calculating of the upper limit
        double bottom = average-2*deviation;        //Defining and calculating of the lower limit
       
        for(int i=0;i<tab.length;i++)           //Checking and replacing departing elements for outliners
        {
            if(tab[i]>upper) tab[i] = upper; //If the value exceeds the upper limit it is converted to it
            if(tab[i]<bottom) tab[i] =bottom; //If the value exceeds the lower limit it is converted to it
        }
        return tab;                                 //returning a corrected tab
    }
}