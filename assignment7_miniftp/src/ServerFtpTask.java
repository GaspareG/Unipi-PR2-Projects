import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.stream.Stream;


public class ServerFtpTask implements Runnable {

	private Socket socket ;
	public ServerFtpTask(Socket ns) {
		this.socket = ns ;
	}

	@Override
	public void run() {
		try {
			socket.setSendBufferSize(2048);
			socket.setTcpNoDelay(true);
			socket.setSoTimeout(200000);
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			
			ArrayList<String> folder = new ArrayList<String>();
			
			while(!socket.isClosed() && !Thread.interrupted())
			{
				String msg = reader.readLine();
				while(msg == null)
					msg = reader.readLine();
				
				String cmd = "" ;
				if(msg.length() != 0)
					cmd = msg.split(" ")[0].toUpperCase() ;
				
				String output = "\n" ;
				System.out.println("[ServerFtpTask] Client["+socket.getInetAddress().getHostAddress()+"] Msg["+msg+"]");
				switch(cmd)
				{
					case "CD":
					case "CWD":
						if(msg.split(" ").length == 1 )
						{
							output += "Manca il nome della cartella!\n" ;
						}
						else
						{
							String fd = msg.split(" ")[1] ;
							if(fd.equalsIgnoreCase(".."))
							{
								if(folder.size()>0) folder.remove(folder.size()-1);
							}
							else
							{
								folder.add(fd);
								File tmp = new File(String.join("/", folder));
								if( !tmp.exists() || !tmp.isDirectory() )
								{
									output += "Directory non esistente...\n" ;
									if(folder.size()>0) folder.remove(folder.size()-1);
								}
							}
						}
						break;
					case "EXIT":
						output += "Connessione chiusa col server\n" ;
						break ;
					case "PWD":
						output += "Cartella attuale: '"+String.join("/",folder)+"'\n" ;
						break ;
					case "DIR": // M$ Style
					case "LIST":
					case "LST":
					case "LS":
						output += "Cartella attuale '"+String.join("/",folder)+"'\n";
						output += "File disponibili:\n";
						Stream<Path> files = Files.list(Paths.get(String.join("/",folder)));
						Iterator<Path> it = files.iterator();
						if(!folder.isEmpty())
							output += "- D .. 0\n" ;
						while(it.hasNext())
						{
							Path att = it.next();
							boolean isF = Files.isDirectory(att);
							int dim = (int) (isF ? 0 : att.toFile().length());
							output += "- "+(isF?"D":"F")+" "+att.getFileName().toString()+" " + dim + "\n";
						}
							
						break ;
					case "RETR":

						if(msg.split(" ").length == 1 )
						{
							output += "Manca il nome del file!\n" ;
						}
						else
						{
							String fd = msg.split(" ")[1] ;
							folder.add(fd);
							String filePath = String.join("/", folder);
							folder.remove(folder.size()-1);
							File toSend = null  ;
							BufferedReader in = null ;
							try 
							{
								toSend = new File(filePath);
								in = new BufferedReader(new FileReader(toSend));
								if(toSend.exists() && !toSend.isDirectory())
								{
									output += "FILE INVIO " + filePath + "\n" ;
									writer.write(output);
									writer.flush();
									
									int count;
									char[] buffer = new char[2048];
									while ((count = in.read(buffer, 0, 2046)) > 0)
									{
										buffer[count++] = '\r' ;
										buffer[count++] = '\n' ;
										writer.write(buffer, 0, count);
									}
									writer.flush();
									//TODO INVIARE BYTE NON CHAR
										
									output = "";
								}
								else
									output += "File non trovato!\n";
							}
							catch(IOException e)
							{
								
							}
							finally
							{
								if(in != null) in.close();
							}
						}
						break ;
					case "HELP":
						output += "MiniFtp - Help\n" ;
						output += "Comandi disponibili:\n" ;
						output += "\t-HELP: stampa questa guida\n" ;
						output += "\t-PWD: stampa la directory attuale\n" ;
						output += "\t-LIST: mostra i file nella directory attuale\n" ;
						output += "\t-RETR nomefile: scarica nomefile in locale\n" ;
						output += "\t-CWD cartella: cambia la directory attuale\n" ;
						output += "\t-EXIT: termina l'esecuzione\n" ;
						break ;
					default:
						output += "Comando non valido!\n";
						break ;
				}
				
				writer.write(output + "NULL\r\n");
				writer.flush();
				
				if(cmd.equalsIgnoreCase("EXIT"))
				{
					socket.close();
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("[ServerFtpTask] Connessione chiusa per IOException...");
		}
		
	}

}
