/*
  Testowanie klasy FFT orac Complex
*/ 


public class test_fft
{

    public static void main(String[] args)
    {
	Complex[] TablicaZDanymi;
	Complex[] Transformata;

	TablicaZDanymi = UtwórzProstokąt2(5,5);

	System.out.println("Dane wejściowe - prostokąt");
	PokażTablicęComplex(TablicaZDanymi);

	Transformata = FFT.fft(TablicaZDanymi);

	System.out.println();
	System.out.println("Transformata FFT sygnału wejściowego");

	PokażTablicęComplex(Transformata);

    }//Koniec main


    public static Complex[] UtwórzProstokąt(int potęga)
    {
	Complex prostokąt[];
	int liczba_elementów;
	int połowa;
	double prostokąt_real[];
	int i;

	liczba_elementów = 1;

	for(i = 0; i < potęga; i++) liczba_elementów = liczba_elementów * 2;

	połowa = liczba_elementów / 2;

	prostokąt_real = new double[liczba_elementów];

	for(i = 0; i < liczba_elementów; i++) //Tworzenie prostokąta rzeczywistego
	{
	    if(i < połowa) prostokąt_real[i] = 1.0; else prostokąt_real[i] = 0.0;
	}//next i

	prostokąt = new Complex[liczba_elementów];

	for(i = 0; i < liczba_elementów; i++) prostokąt[i] = new Complex(prostokąt_real[i],0.0);

	return prostokąt;
	
    }//Koniec metody

    public static Complex[] UtwórzProstokąt2(int potęga, int IleJedynek)
    {
	Complex prostokąt[];
	int liczba_elementów;
	double prostokąt_real[];
	int i;

	liczba_elementów = 1;

	for(i = 0; i < potęga; i++) liczba_elementów = liczba_elementów * 2;


	prostokąt_real = new double[liczba_elementów];

	for(i = 0; i < liczba_elementów; i++) //Tworzenie prostokąta rzeczywistego
	{
	    if(i < IleJedynek) prostokąt_real[i] = 1.0/((double)liczba_elementów); else prostokąt_real[i] = 0.0;
	}//next i

	prostokąt = new Complex[liczba_elementów];

	for(i = 0; i < liczba_elementów; i++) prostokąt[i] = new Complex(prostokąt_real[i],0.0);

	return prostokąt;
	
    }//Koniec metody

    public static void PokażTablicęComplex(Complex[] Tablica)
    {
	int i;
	String tekst;

	for(i = 0; i < Tablica.length; i++)
	{
	    tekst = "Tablica["+i+"]= " + Tablica[i].re() + " +j " + Tablica[i].im() + " moduł= " + Tablica[i].abs();

	    tekst = tekst.replace('.',',');

	    System.out.println(tekst);
	}//next i

    }//Koniec pokazywania tablicy Complex

}//Koniec klasy