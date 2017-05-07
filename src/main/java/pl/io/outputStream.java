package pl.io;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.ugens.Envelope;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.WavePlayer;
import net.beadsproject.beads.data.BufferFactory;


/**
 * Sine-type buffer with amplitude 0.9 * 
 */
class ChangedSineBuffer extends BufferFactory
{

    public Buffer generateBuffer(int bufferSize)
    {
    	Buffer b = new Buffer(bufferSize);
        for(int i = 0; i < bufferSize; i++) {
            b.buf[i] = 0.9f * (float)Math.sin(2.0 * Math.PI * (double)i / (double)bufferSize);
        }
    	return b;
    }

    public String getName()
    {
    	return "ChangedSine";
    }
}

/**
 * Class with static methods to generate sound.
 * @version 2.2.2
 */
public class outputStream{	
	
	/**
	 * This method generates sweep signal.
	 * @param startFreq Initial frequency of signal
	 * @param endFreq Final frequency of signal
	 * @param time Determines how long signal should be generating
	 * @throws InterruptedException 
	 */
	static String getTime()
	{
		return "[Curr. time[ms]: " + System.currentTimeMillis() + "]";		
	}
	
	static void sweep(final float startFreq, final float endFreq, final float time)
	{
		Thread t = new Thread()
		{
			public void run()
			{
				AudioContext ac = new AudioContext();
				Envelope e1 = new Envelope(ac, startFreq);
				e1.addSegment(endFreq, time);
				WavePlayer wp1 = new WavePlayer(ac, e1, Buffer.SINE);
				Gain g = new Gain(ac, 1, 0.1f);
				g.addInput(wp1);
				ac.out.addInput(g);
				
				System.out.println(getTime() + " Starting sweep signal"); 
				
				ac.start();
				try
				{
					Thread.sleep((long) time);
				} catch (InterruptedException e)
				{
					System.err.println(e.getMessage());
				}
				ac.stop();
				System.out.println(getTime() + " End of sweep signal"); 
			}
		};
		t.start();
	}
	
	/**
	 * This method generates sinusoidal signal
	 * @param freq Sets frequency of signal
	 * @param time Determines how long signal should be generating
	 * @throws InterruptedException 
	 */
	static void sin(final float freq, final long time)
	{
		Thread t = new Thread()
		{
			public void run()
			{
				AudioContext ac = new AudioContext();
				Buffer CSIN = new ChangedSineBuffer().getDefault();
				WavePlayer wp = new WavePlayer(ac, freq, CSIN);
				ac.out.addInput(wp);
				System.out.println(getTime() + " Starting sin signal"); 
				ac.start();
				try
				{
					Thread.sleep(time);
				} catch (InterruptedException e)
				{
					System.err.println(e.getMessage());
				}
				ac.stop();
				System.out.println(getTime() + " End of sin signal"); 
			}
		};
		t.start();
	}
}
