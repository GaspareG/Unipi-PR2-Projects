package TwistServer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TwistGame implements Runnable {

	private Integer port ;
	private List<String> dictionary ;
	private TwistOnline onlineManager ;
	private TwistScoreboard scoreboardManager ;
	
	public TwistGame(Integer gamePort, List<String> dictionaryWords, TwistOnline onlineManager,
			TwistScoreboard scoreboardManager) {
		this.port = gamePort ;
		this.dictionary = dictionaryWords ;
		this.onlineManager = onlineManager ;
		this.scoreboardManager = scoreboardManager ;
	}

	@Override
	public void run() {		
		ServerSocket ss = null ;
		ExecutorService es = null ;
		try {
			ss = new ServerSocket();
			ss.bind(new InetSocketAddress(InetAddress.getLocalHost(), port));
			ss.setReceiveBufferSize(1024);
			
			es = Executors.newFixedThreadPool(4);

			while(!Thread.interrupted())
			{
				Socket ns = ss.accept();
				TwistGameTask task = new TwistGameTask(onlineManager,this,ns);
				es.execute(task);
			}
		} catch (IOException e) {
			if(es != null) es.shutdownNow();
		}
	}

	public void setupMatch( List<String> invitati )
	{
		System.out.println("INVITATI " + invitati.size());
	}
}
