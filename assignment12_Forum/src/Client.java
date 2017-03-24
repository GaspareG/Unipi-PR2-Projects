import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Vector;

public class Client {
	

	public static int REGISTRY_PORT=2020;
	public static String REGISTRY_HOST="localhost";
	
	public static void main(String[] args) {
		
		System.out.println("[Client] Starting client!");

		try {
			Registry registry =
			LocateRegistry.getRegistry(REGISTRY_HOST, REGISTRY_PORT);
			Forum forum = (Forum) registry.lookup(Forum.REMOTE_OBJECT_NAME);
			System.out.println("[Client] forum retrivied!");
			
			String command = "" ;
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

			do {
				System.out.println("Inserisci comando: [help per la guida]\n");
				command = br.readLine().toLowerCase();
				switch(command)
				{
					case "help":
						System.out.println("Guida comandi:");
						System.out.println("- Help: mostra questa guida");
						System.out.println("- Create: crea un nuovo forum non esistente");
						System.out.println("- Write: invia un messaggio ad un forum esistente");
						System.out.println("- Read: legge tutti i messaggi di un forum esistente");
						System.out.println("- Add: aggiunge un forum alle notifiche");
						System.out.println("- Exit: termina il programma");
					break;
					case "create":
						System.out.println("[Client] Inserisci il nome del nuovo forum!");
						command = br.readLine().toLowerCase();
						forum.addForum(command);
						System.out.println("[Client] Forum creato correttamente!");
					break;
					case "write":
						System.out.println("[Client] Inserisci il nome del forum dove scrivere! ");
						command = br.readLine().toLowerCase();
						String fname = new String(command);
						System.out.println("[Client] Inserisci il messaggio da scrivere nel forum!");
						command = br.readLine().toLowerCase();
						String fmsg = new String(command);
						forum.addMessage(fname, fmsg);
						System.out.println("[Client] Messaggio inviato correttamente!");
					break;
					case "read":
						System.out.println("[Client] Inserisci il nome forum da leggere!");
						command = br.readLine().toLowerCase();
						Vector<String> mess = forum.getMessage(command);
						System.out.println("[Client] Messaggi del forum:");
						for(String m : mess)
							System.out.println("\t- "+m);
						System.out.println("[Client] Iscrizione al forum effettuata correttamente!");
					break;
					case "add":
						System.out.println("[Client] Inserisci il nome forum al quale iscriversi!");
						command = br.readLine().toLowerCase();
						// Registro callback!
						RNotifica notify = new RNotifica("notify-"+command);
						Notifica stb = (Notifica) UnicastRemoteObject.exportObject(notify, 0);
						forum.subForum(command, stb);
						System.out.println("[Client] Iscrizione al forum effettuata correttamente!");
					break;
					case "exit":
						System.out.println("[Client] Esco...");
					break;
				}
			} while(!command.equalsIgnoreCase("exit"));
						
		} catch (RemoteException e) {
			System.out.println("[Client] Error in client: "+e.getMessage());
		} catch (NotBoundException e) {
			System.out.println("[Client] Remote object not found: "+e.getMessage());
		} catch (IOException e) {
			System.out.println("[Client] Error in input reading: "+e.getMessage());
		}
		
		System.out.println("[Client] Closed client!");
	}
		
}
