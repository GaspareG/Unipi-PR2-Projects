package TwistServer;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class TwistScoreboardTask implements Runnable {

	private Socket socket ;
	private String scoreboard ;
	
	public TwistScoreboardTask(String scoreboard, Socket ns) {
		this.socket = ns ;
		this.scoreboard = scoreboard;
	}
		
	@Override
	public void run() {
		try {
			
			socket.setSendBufferSize(2048);
			socket.setTcpNoDelay(true);
			socket.setSoTimeout(200000);
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			writer.write(scoreboard + "\r\n");
			writer.flush();
			socket.close();
		
		} catch (IOException e) {
			System.out.println("[Server] Connessione chiusa per IOException...");
		}
	}

}
