import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class TrovaPrezziClient implements Runnable {

	private String serverAddr ;
	private int port ;
	
	public TrovaPrezziClient(String addr, int port) {
		this.serverAddr = addr ;
		this.port = port ;
	}

	@Override
	public void run() {
		
		SocketAddress server = null ;
		Socket conn = null ;
		BufferedReader in = null ;

		System.out.println("[TrovaPrezziClient] Avvio client, connessione al server...");
		
		try {
			in = new BufferedReader(new InputStreamReader(System.in));
			server = new InetSocketAddress(serverAddr,port);
			conn = new Socket();
			conn.connect(server);

			System.out.println("[TrovaPrezziClient] Connesso al server "+conn.getInetAddress().getHostAddress());
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
			
			while(!Thread.interrupted() && !conn.isClosed())
			{

				System.out.println("[TrovaPrezziClient] Inserisci il prodotto da ricercare (o exit per terminare)");
				System.out.print("<< ");
				String msg = in.readLine();
				while(msg == null)
					msg = in.readLine();
				
				if(msg.equalsIgnoreCase("exit"))
					break ;
				writer.write(msg + "\r\n");
				writer.flush();
				
				String input = "" ;
				do 
				{
					input = reader.readLine();
					if(input != null && input.length() > 0)
					{
						System.out.println(">> "+input);
					}
				}
				while(input != null && input.length() > 0);
				
			}
			conn.close();
			System.out.println("[TrovaPrezziClient] Connessione con il server terminata");
		}
		catch (IOException e) 
		{
			System.out.println("[TrovaPrezziClient] Errore IO");
			e.printStackTrace();
		}
	}

}
