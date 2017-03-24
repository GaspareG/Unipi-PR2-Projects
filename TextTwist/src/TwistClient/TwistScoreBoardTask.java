package TwistClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import javax.swing.JLabel;

public class TwistScoreBoardTask implements Runnable {

	private JLabel content ;
	private String serverUrl ;
	private Integer port ;
	public TwistScoreBoardTask(JLabel lCredits, String serverUrl, Integer port) {
		this.content = lCredits ;
		this.serverUrl = serverUrl;
		this.port = port;
	}

	@Override
	public void run() {
		String toSet = "" ;
		SocketAddress server = null ;
		Socket conn = null ;
		try {
			server = new InetSocketAddress(serverUrl, port);
			conn = new Socket();
			conn.connect(server);
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			toSet += reader.readLine() ;
			conn.close();
		}
		catch (IOException e) {
			
		}
		content.setText("<html>"+toSet+"</html>");
	}

}
