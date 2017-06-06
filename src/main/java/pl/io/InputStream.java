package pl.io;

import java.io.IOException;
import javax.sound.sampled.*;


public class InputStream 
{	
	
    /**
     * @param recordingTime
     * @return
     * @throws Exception
     */
    public static double[][] reading(float recordingTime) throws Exception
    {
        int bufferSize =  (int) (48000 * 4 * recordingTime/1000.);
        final byte[] fromStream = new byte[bufferSize];

        //Create audio format with 48000 sampleRate
        AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 48000, 16, 2, 4, 48000, false);
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);  
        
        if(!AudioSystem.isLineSupported(info))
            System.err.println("Line unsupported");

        //Create target line and open it
        final TargetDataLine targetLine = (TargetDataLine)AudioSystem.getLine(info);
        targetLine.open();
       
        System.out.println("Recording has started...");
        targetLine.start();

        //Create separate thread
        Thread thread = new Thread(){
            @Override public void run(){
                AudioInputStream audioStream = new AudioInputStream(targetLine);
                try{
                    //Read stream from input audioStream
                	audioStream.read(fromStream);
                }
                catch (IOException e){
                	e.printStackTrace();
                }
                System.out.println("Recording stopped");
            }
        };
        //Execute thread with some sleep
        thread.start();
        Thread.sleep((long) (recordingTime + 500));
        targetLine.stop();
        targetLine.close();
        System.out.println("Done.");

        //Result is double array
        double[][] result = regroupData(fromStream);
       
        return result;    
    }
   
    /**
     * @param fromStream
     * @return
     */
    private static double[][] regroupData(byte[] fromStream){
        int channelShortSize = fromStream.length/4;
       
        short toChannel1, toChannel2;
        short[] channel1 = new short[channelShortSize];
        short[] channel2 = new short[channelShortSize];
        int totalSamples = 0;

        //Convert to big endian divided data on two channels
        for(int i = 0; i<fromStream.length; i=i+4, totalSamples++){
            toChannel1 = (short)(fromStream[i+1] << 8);
            toChannel1 = (short)(toChannel1 + fromStream[i]);
           
            toChannel2 = (short)(fromStream[i+3] << 8);
            toChannel2 = (short)(toChannel2 + fromStream[i+2]);
           
            channel1[totalSamples] = toChannel1;
            channel2[totalSamples] = toChannel2;
        }
        short[][] shortChannels = new short[][]{
            channel1,channel2,
        };
       
        double[][] doubleChannels = new double[2][channelShortSize];
        for(int j = 0; j<channelShortSize;j++){
            doubleChannels[0][j] = -shortChannels[0][j];
            doubleChannels[1][j] = -shortChannels[1][j];
        }

        return doubleChannels;
    }
}
