
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Vector;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class PingClient {

	static final int MAX_LENGTH=512;
	

	public static void main(String[] args) 
	{
		////////////////////////////////////////////////////////////////
		// Parsing dei parametri									  //
		////////////////////////////////////////////////////////////////
		String server ;
		int port ;

		if(args.length == 0)
		{
			System.out.println("[PingClient] ERR -arg 0");
			return ;
		}	
		server = args[0] ;
		InetAddress serverAddr;
		try {
			serverAddr = InetAddress.getByName(server);
		} catch (UnknownHostException e1) {
			System.out.println("[PingClient] ERR -arg 0");
			return ;
		}
		
		if(args.length == 1 )
		{
			System.out.println("[PingClient] ERR -arg 1");
			return ;
		}
		try
		{
			port = new Integer(args[1]);
		}
		catch(NumberFormatException e)
		{
			System.out.println("[PingClient] ERR -arg 1");
			return ;
		}
		////////////////////////////////////////////////////////////////
		// Inizio Ping Client                                         //
		////////////////////////////////////////////////////////////////

		Vector<Future<Long>> risultati = new Vector<Future<Long>>();
		
		ExecutorService es= Executors.newSingleThreadExecutor();
		SocketAddress serverSocket;
		
		try {
			serverSocket = new InetSocketAddress(serverAddr, port);
			DatagramSocket client= new DatagramSocket();
			
			int messbyte = ("PING 0 "+ System.currentTimeMillis()).length();
			System.out.println("PING "+serverAddr.getHostName()+" ("+serverAddr.getHostAddress()+") "+messbyte+" data bytes");
			
			// Creo e aggiungo i 10 ping
			for(int i=0; i<10; i++)
				risultati.add(es.submit(new PingClientTask(client,serverSocket,serverAddr,i)));
			es.shutdown();
			es.awaitTermination(1, TimeUnit.HOURS);
			
			// Stats data
			int persi = 0 ;
			int tempo = 0 ;
			int ricevuti = 0 ;
			int TsMin = 2000 ;
			int TsMax = 0 ;
			
			for(Future<Long> req : risultati)
			{
				if( req.get() == -1 ) persi++;
				else 
				{
					int att = req.get().intValue();
					tempo += att;
					ricevuti++ ;
					TsMin = Math.min(TsMin, att);
					TsMax = Math.max(TsMax, att);
				}
			}
			
			int perc = (100*persi)/(ricevuti+persi) ;
			float TsAvg = ricevuti == 0 ? 0 : ((int)(100*tempo/ricevuti))/100.f;
			
			// Stats output
			System.out.println("");
			System.out.println("--- "+serverAddr.getHostName()+" ping statistics ---");
			System.out.println((persi+ricevuti)+" packets transmitted, "+ricevuti+" received, "+perc+"% packet loss, time "+tempo+"ms");
			System.out.println("rtt min/avg/max = "+TsMin+"/"+TsAvg+"/"+TsMax+" ms");
			
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			System.out.println("[PingClient] Ping client interrotto");
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		finally {
			if(es != null) es.shutdownNow();
		}
	}
}
