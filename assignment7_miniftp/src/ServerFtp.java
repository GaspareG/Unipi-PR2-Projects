import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerFtp {

	static final int THREAD = 4 ;
	
	public static void main(String args[])
	{
	
		int defaultPort = 2000 ;
		if(args.length > 0)
		{
			try 
			{
				int nport = new Integer(args[0]);
				defaultPort = nport ;
			}
			catch( NumberFormatException e)
			{
				System.err.println("[ServerFtp] Err: -arg 0");
			}
		}
		
		ServerSocket ss = null ;
		ExecutorService es = null ;
		try {
			ss = new ServerSocket();
			ss.bind(new InetSocketAddress(InetAddress.getLocalHost(), defaultPort));
			ss.setReceiveBufferSize(1024);
			
			es = Executors.newFixedThreadPool(THREAD);

			System.out.println("[ServerFtp] In ascolto...");
			while(!Thread.interrupted())
			{
				Socket ns = ss.accept();
				System.out.println("[ServerFtp] Gestisco ["+ns.getInetAddress().getHostAddress()+"]");
				ServerFtpTask task = new ServerFtpTask(ns);
				es.execute(task);
			}
		} catch (IOException e) {
			if(es != null) es.shutdownNow();
		}
		
	}
}
