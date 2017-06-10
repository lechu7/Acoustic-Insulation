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
public class test_FFT extends TestCase {
    
    public test_FFT(String testName) {
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
     * Test of fft method, of class FFT.
     */
    public void testFft() {
        System.out.println("fft");
        Complex[] x = new Complex[4];
        Complex[] expResult = new Complex[4];
        Complex[] result;
        Complex difference;
        int i;
        boolean czy_rowne;
        
        //Przykładowe wartości x
        x[0] = new Complex(-0.03480425839330703,0);
        x[1] = new Complex(0.07910192950176387,0);
        x[2] = new Complex(0.7233322451735928,0);
        x[3] = new Complex(0.1659819820667019,0);
        
        //wynik FFT
        result = FFT.fft(x);
        
        //oczekiwany wynik FFT
        expResult[0] = new Complex(0.9336118983487516,0);
        expResult[1] = new Complex(-0.7581365035668999,0.08688005256493803);
        expResult[2] = new Complex(0.44344407521182005,0);
        expResult[3] = new Complex(-0.7581365035668999,-0.08688005256493803);
        

        czy_rowne = true;
        i = 0;
        while(i < x.length && czy_rowne == true) {
            difference = expResult[i].minus(result[i]);
            if(difference.re() < result[i].re()-0.0001 || difference.re() > result[i].re() + 0.0001 || difference.im() < result[i].im()-0.0001 || difference.im() > result[i].im() + 0.0001) czy_rowne = true;
            i++;
        }//koniec pętli sprawdzającej poprawność FFT
        assertEquals(czy_rowne, true);
    }//koniec testu FFT
    
}
