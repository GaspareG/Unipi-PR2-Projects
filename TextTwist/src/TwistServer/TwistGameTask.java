package TwistServer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class TwistGameTask implements Runnable {

	private Socket socket ;
	private TwistOnline onlineManager ;
	private TwistGame twistGame ;
	
	public TwistGameTask(TwistOnline onlineManager, TwistGame twistGame, Socket ns) {
		this.socket = ns ;
		this.onlineManager = onlineManager ;
		this.twistGame = twistGame ;
	}

	@Override
	public void run() {
		try {
			
			socket.setSendBufferSize(2048);
			socket.setTcpNoDelay(true);
			socket.setSoTimeout(200000);
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			String token = reader.readLine();
			String command = reader.readLine();
			String option = reader.readLine();
			
			System.out.println("TOKEN" + token);
			System.out.println("COMMAND" + command);
			System.out.println("OPTION" + option);
			
			writer.flush();
			
			socket.close();
		
		} catch (IOException e) {
			System.out.println("[TwistServer] Connessione chiusa per IOException...");
		}
	}
}
