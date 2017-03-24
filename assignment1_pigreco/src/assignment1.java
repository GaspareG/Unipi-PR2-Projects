
public class assignment1 {

	public static void main(String[] args) throws InterruptedException 
	{
		int msTimeOut = 60000 ;			// default timeout: 60 seconds
		double accuracy = 0.00000001 ;	// default accuracy: 10^-10
			
		// parse input from args
		if(args.length >= 1)
		{
			try 
			{
				int tempTimeOut = new Integer(args[0]);
				if(tempTimeOut > 0) msTimeOut = tempTimeOut ;
			}
			catch(NumberFormatException c){ }
			
			if(args.length >= 2)
			{
				try 
				{
					double tempAccuracy = new Double(args[1]);
					if(tempAccuracy != 0) accuracy = tempAccuracy ;
				}
				catch(NumberFormatException c){ }
			}
		}

		// Print input data
		System.out.println("Timeout for thread:\t"+ msTimeOut + "ms");
		System.out.println("Accuracy for Pi:\t"+ accuracy);
		System.out.println("");
		
		// Create, run, join and interrupt GregoryLeibnizThread Thread
		GregoryLeibnizThread runnable = new GregoryLeibnizThread(accuracy);
		Thread t = new Thread(runnable);
		t.start();
		t.join(msTimeOut);
		if(t.isAlive()) t.interrupt();	
	}

}
