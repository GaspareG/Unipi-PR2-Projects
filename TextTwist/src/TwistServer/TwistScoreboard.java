package TwistServer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import TwistUtility.TwistJsonConf;

public class TwistScoreboard extends Thread{

	private TwistJsonConf scoreboard ;
	private Integer port;
	
	public TwistScoreboard(TwistJsonConf scoreBoard, Integer port) {
		
		this.scoreboard = scoreBoard ;
		this.port = port ;
		
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
				TwistScoreboardTask task = new TwistScoreboardTask(this.generateScoreboard(),ns);
				es.execute(task);
			}
		} catch (IOException e) {
			if(es != null) es.shutdownNow();
		}
	}
	
	@SuppressWarnings("unchecked")
	public String generateScoreboard()
	{
		String out = "" ;
		Object[] a = scoreboard.getContent().entrySet().toArray();
		Arrays.sort(a, new Comparator<Object>() {
		    public int compare(Object o1, Object o2) {
		        return ((Map.Entry<String, Long>) o2).getValue()
		                   .compareTo(((Map.Entry<String, Long>) o1).getValue());
		    }
		});
		int i = 1 ;
		for (Object e : a) {
		     out += (i++) + ") " + ((Map.Entry<String, Integer>) e).getValue() + "pt "
		            + ((Map.Entry<String, Integer>) e).getKey() + "<br>";
		}
		return out ;
	}
	
	public Integer getScoreByUsername(String username)
	{
		Integer out = 0;
		Long val = scoreboard.getLong(username);
		if( val != null ) out = val.intValue();
		return out;
	}
	
	public void addScoreToUsername(String username, Integer toAdd)
	{
		Integer newScore = toAdd + getScoreByUsername(username);
		scoreboard.setLong(username, newScore.longValue());
	}
}
