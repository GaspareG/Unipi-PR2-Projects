import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class ClientFtp {


	public static void main(String args[])
	{
		String indirizzo = "" ;
		int porta = 0 ;
		
		if(args.length > 0)
			indirizzo = args[0] ;
		else
			System.err.println("[ClientFtp] Err: -arg 0");
		
		if(args.length > 1)
		{
			try 
			{
				porta = new Integer(args[1]);
			}
			catch(NumberFormatException e)
			{
				System.err.println("[ClientFtp] Err: -arg 1");	
			}
		}
		else
			System.err.println("[ClientFtp] Err: -arg 1");
		
		SocketAddress server = null ;
		Socket conn = null ;
		BufferedReader in = null ;
		
		try {
			in = new BufferedReader(new InputStreamReader(System.in));
			server = new InetSocketAddress(indirizzo,porta);
			conn = new Socket();
			conn.connect(server);

			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
			
			System.out.println("[ClientFtp] Connesso al server ftp...");
			
			while(!conn.isClosed())
			{
				System.out.print("<< ");
				String msg = in.readLine();
				while(msg == null)
					msg = in.readLine();
				
				writer.write(msg + "\r\n");
				writer.flush();
				
				
				String input ;
				boolean ex = false ;
				do 
				{
					input = reader.readLine();
					if(input != null && input.startsWith("FILE INVIO") )
					{
						ex = true ;
						gestisciDownload(input,reader);
					}
					else if(input != null && !input.equalsIgnoreCase("NULL"))
					{
						System.out.println("Server >> " + input);
					}
					else
						ex = true ;
				}
				while(!ex);
				
				if(msg.equalsIgnoreCase("exit"))
					conn.close();
			}
			
			conn.close();
		} catch (IOException e) {
			System.out.println("[ClientFtp] Errore IO");
			e.printStackTrace();
		}
		
	}

	private static void gestisciDownload(String input, BufferedReader reader) throws IOException {
		
		String nomeFile = "" ;
		if(input.lastIndexOf("/") != -1 )
		{
			nomeFile = input.substring(input.lastIndexOf("/")+1);
		}
		else
		{
			nomeFile = input.replaceFirst("FILE INVIO ","");
		}
		nomeFile = "client_" + nomeFile ;
		System.out.println("[ClientFtp] Ricevo file " + nomeFile);
		String contenuto = "" ;
		boolean ex = false ;
		do 
		{
			String riga = reader.readLine();
			if(riga != null && !riga.equalsIgnoreCase("NULL"))
				contenuto += (contenuto.length() != 0 ? "\n" : "" ) + riga ;
			else
				ex = true ;
		}
		while(!ex);
		System.out.println("[ClientFtp] Scrivo file " + nomeFile);		
		try (PrintStream out = new PrintStream(new FileOutputStream(nomeFile))) {
		    out.print(contenuto);
		    out.flush();
		    out.close();
		}
		
		System.out.println("[ClientFtp] "+contenuto.length()+" byte scritti nel file "+nomeFile);
	}
}
