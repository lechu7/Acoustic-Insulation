package pl.io;

public class App 
{
	/**
	 * 
	 * @param args
	 * @throws Exception
	 */
    public static void main( String[] args ) throws Exception
    {
		byte[] response = inputStream.reading();
		for(int i = 0; i<response.length; i++){
			System.out.println(response[i]);
		}
    }
}
