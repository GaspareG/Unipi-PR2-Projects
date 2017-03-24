import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

public class TrovaPrezziServerTask implements Runnable {

	private Socket socket;
	private TrovaPrezziServer server ;
	
	public TrovaPrezziServerTask(Socket ns, TrovaPrezziServer trovaPrezziServer) {
		this.socket = ns ;
		this.server = trovaPrezziServer ;
	}

	@Override
	public void run() {
		try {
			socket.setSendBufferSize(2048);
			socket.setTcpNoDelay(true);
			socket.setSoTimeout(200000);
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			
			while(!socket.isClosed() && !Thread.interrupted())
			{
				String msg = reader.readLine();
				while(msg == null)
					msg = reader.readLine();
				
				writer.write("TODO");
				writer.flush();
				
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("[ServerFtpTask] Connessione chiusa per IOException...");
		}
		
	}
	

}
