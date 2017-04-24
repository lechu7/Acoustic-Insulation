package pl.io;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.ugens.Envelope;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.WavePlayer;

public class outputStream{
	
	void sweep()
	{
		AudioContext ac = new AudioContext();
		Envelope e1 = new Envelope(ac, 20);
		e1.addSegment(20000, 30000);
		WavePlayer wp1 = new WavePlayer(ac, e1, Buffer.SINE);
		Gain g = new Gain(ac, 1, 0.1f);
		g.addInput(wp1);
		ac.out.addInput(g);
		ac.start();
	}
	
	public static void main(String[] args) {
		try
		{
			outputStream toTest = new outputStream();
			toTest.sweep();
		}
		catch(Exception e)
		{
			
		}
	}

}
