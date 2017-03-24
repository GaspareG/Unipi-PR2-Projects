
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PingServer {
	
	// Costanti per il server
	static int MAX_LENGTH=512;
	static int PORT=2000;
	static int THREADS=4;
	static float PACKETLOSS = 0.25f ;
	static Random RND ;
	public static void main(String[] args) 
	{
		////////////////////////////////////////////////////////////////
		// Parsing dei parametri									  //
		////////////////////////////////////////////////////////////////
		RND = new Random(System.currentTimeMillis());
		if(args.length == 0)
		{
			System.out.println("[PingServer] ERR -arg 0");
			return ;
		}
		
		try
		{
			int port = new Integer(args[0]);
			PORT = port ;
		}
		catch(NumberFormatException e)
		{
			System.out.println("[PingServer] ERR -arg 0");
			return ;
		}
		
		if(args.length >= 2)
		{
			try
			{
				long seed = new Long(args[1]);
				RND = new Random(seed);
			}
			catch(NumberFormatException e)
			{
				System.out.println("[PingServer] ERR -arg 1");
				return ;
			}
		}
		
		if(args.length >= 3)
		{
			try
			{
				int loss = new Integer(args[2]);
				PACKETLOSS = loss/100.f ;
			}
			catch(NumberFormatException e)
			{
				System.out.println("[PingServer] ERR -arg 2");
				return ;
			}
		}
		
		////////////////////////////////////////////////////////////////
		// Inizio ping server                                         //
		////////////////////////////////////////////////////////////////
		System.out.println("[PingServer] Avvio ping server");
		ExecutorService es = null ;
		try(DatagramSocket server = new DatagramSocket(PORT);)
		{
			es = Executors.newFixedThreadPool(THREADS);
			ArrayList<PingServerTask> tasks= new ArrayList<>();
			for (int i=0;i<THREADS;i++)
				tasks.add(new PingServerTask(server));
			es.invokeAll(tasks);
			es.shutdown();
		} 
		catch(SocketException e) 
		{
			System.err.println("[PingServer] Error creating socket: "+e.getMessage());
		} 
		catch(InterruptedException e) 
		{
			System.out.println("[PingServer] Ping Server interrotto...");
		}
		finally
		{
			if(es != null) es.shutdownNow();			
		}
		
	}

}
