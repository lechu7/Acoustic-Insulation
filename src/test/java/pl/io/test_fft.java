/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.io;

import junit.framework.TestCase;

/**
 *
 * @author Mateusz Kubiakowski
 */
public class test_PreparingFFT extends TestCase {
    
    public test_PreparingFFT(String testName) {
        super(testName);
    }
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of FillZeros method, of class PreparingFFT.
     */
    public void testFillZeros() {
        System.out.println("FillZeros");
        double[] x = new double[]{1,2,3,4,5,3,2,1,0,1};
        double[] expResult = new double[]{1,2,3,4,5,3,2,1,0,1,0,0,0,0,0,0};
        double[] result = PreparingFFT.FillZeros(x);
        boolean czy_rowne = true;
        
        for(int i=0;i<result.length;i++) {
            if(expResult.length != result.length || expResult[i] != result[i]) czy_rowne = false;
        }
        assertEquals(czy_rowne, true);
    }

    /**
     * Test of DataPreparingForFFT method, of class PreparingFFT.
     */
    public void testDataPreparingForFFT() {
        System.out.println("DataPreparingForFFT");
        //przykładowe wartości danych
        double[] x = new double[]{1,2,3,4,5,3,2,1,0,1};
        //rozszerzone wartości danych
        double[] xZeros = new double[]{1,2,3,4,5,3,2,1,0,1,0,0,0,0,0,0};
        
        Complex[] expResult = new Complex[xZeros.length];
        //wynik DataPreparingForFFT
        Complex[] result = PreparingFFT.DataPreparingForFFT(x);
        
        //oczekiwany wynik DataPreparingForFFT
        expResult[0] = new Complex(1.0/16.0,0.0);
        expResult[1] = new Complex(2.0/16.0,0.0);
        expResult[2] = new Complex(3.0/16.0,0.0);
        expResult[3] = new Complex(4.0/16.0,0.0);
        expResult[4] = new Complex(5.0/16.0,0.0);
        expResult[5] = new Complex(3.0/16.0,0.0);
        expResult[6] = new Complex(2.0/16.0,0.0);
        expResult[7] = new Complex(1.0/16.0,0.0);
        expResult[8] = new Complex(0.0/16.0,0.0);
        expResult[9] = new Complex(1.0/16.0,0.0);
        for(int i=10;i<xZeros.length;i++) {
            expResult[i] = new Complex(0.0/16.0,0.0);
        }//koniec oczekiwanego wyniku DataPreparingForFFT
        
        boolean czy_rowne = true;
        int i = 0;
        Complex difference;
        while(i<xZeros.length && czy_rowne == true) {
            difference = expResult[i].minus(result[i]);
            if(difference.re() < result[i].re()-0.0001 || difference.re() > result[i].re() + 0.0001 || difference.im() < result[i].im()-0.0001 || difference.im() > result[i].im() + 0.0001) czy_rowne = true;
            i++;
        }//koniec pętli sprawdzającej poprawność DataPreparingForFFT
        assertEquals(czy_rowne, true);
    }//koniec testu DataPreparingForFFT

    /**
     * Test of FrequencyDenormalization method, of class PreparingFFT.
     */
    public void testFrequencyDenormalization() {
        System.out.println("FrequencyDenormalization");
        double n = 5.0;
        double fs = 10.0;
        double N = 16.0;
        double expResult = 3.125;
        double result = PreparingFFT.FrequencyDenormalization(n, fs, N);
        assertEquals(expResult, result, 0.0);
    }//koniec testu FrequencyDenormalization

    /**
     * Test of EnergySpectrum method, of class PreparingFFT.
     */
    public void testEnergySpectrum() {
        System.out.println("EnergySpectrum");
        
        //Przykładowe dane wejściowe
        Complex[] X = new Complex[4];
        int original_length = 4;
        X[0] = new Complex(0.9336118983487516,0.0);
        X[1] = new Complex(-0.7581365035668999,0.08688005256493803);
        X[2] = new Complex(0.44344407521182005,0.0);
        X[3] = new Complex(-0.7581365035668999,-0.08688005256493803);
        double[] expResult = new double[3];
        
        //oczekiwany wynik końcowy
        expResult[0] = 0.8716311767383597;
        expResult[1] = 2.3292764062973217;
        expResult[2] = 0.7865705913618652;
        
        //otrzymany wynik końcowy
        double[] result = PreparingFFT.EnergySpectrum(X, original_length);
        int i = 0;
        boolean czy_rowne = true;
        double difference;
        while(i<result.length && czy_rowne == true) {
            difference = expResult[i]-result[i];
            if(difference < -0.00001 || difference > 0.00001) czy_rowne = true;
            i++;
        }//koniec pętli sprawdzającej poprawność EnergySpectrum
        assertEquals(czy_rowne, true);
    }//koniec testu EnergySpectrum

    /**
     * Test of Power_dB method, of class PreparingFFT.
     */
    public void testPower_dB() {
        System.out.println("Power_dB");
        double P1 = 0.5927;
        double P0 = 1;
        double expResult = -2.271650727;
        double result = PreparingFFT.Power_dB(P1, P0);
        assertEquals(expResult, result, 0.0001);
    }//koniec testu Power_dB
    
}
