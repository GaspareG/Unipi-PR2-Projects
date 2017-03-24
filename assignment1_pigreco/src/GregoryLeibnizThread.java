import java.util.Date;


public class GregoryLeibnizThread implements Runnable 
{

	private double accuracy ;
	private double threadPi ;
	
	public GregoryLeibnizThread(double acc)
	{
		this.accuracy = acc ;	
	}
	
	@Override
	public void run() 
	{
		this.threadPi = 0 ;
		int sign = 1 ;
		int den = 1 ;
		int step = 0 ;
		
		Date inizio = new Date();
		long tsInizio = inizio.getTime();
		
		while( !Thread.currentThread().isInterrupted() && Math.abs(Math.PI - this.threadPi) > this.accuracy)
		{
			this.threadPi += 4.*sign/den ;
			sign *= -1 ;
			den += 2 ;
			step++ ;
		}

		Date fine = new Date();
		long tsFine = fine.getTime();
		
		// Exit for timeout or reaching accuracy
		System.out.println("[Thread] Calculated PI:\t" + this.threadPi);
		System.out.println("[Thread] Math.PI:\t" + Math.PI);
		System.out.println("[Thread] Accuracy:\t" + Math.abs(Math.PI - this.threadPi) );
		System.out.println("[Thread] Step:\t\t" + step);
		System.out.println("[Thread] Time:\t\t~" + (tsFine-tsInizio) + "ms" );
	
	}


}
