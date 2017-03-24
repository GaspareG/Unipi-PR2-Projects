package assignment11_CongressoRmi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {
	

	public static int REGISTRY_PORT=2020;
	public static String REGISTRY_HOST="localhost";
	
	public static void main(String[] args) {
		
		System.out.println("[Client] Starting client!");

		try {
			Registry registry =
			LocateRegistry.getRegistry(REGISTRY_HOST, REGISTRY_PORT);
			Congresso cong = (Congresso) registry.lookup(Congresso.REMOTE_OBJECT_NAME);
			System.out.println("[Client] Congress schedule:");
			System.out.println(cong.getCongressSchedule());
			
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
						System.out.println("- Add: aggiunge uno speaker ad una sessione");
						System.out.println("- Get: stampa il programma del congresso");
						System.out.println("- Exit: esce dal programma");
					break;
					case "add":
						System.out.println("Inserisci il giorno (da 0 a "+(cong.getDays()-1)+") ");
						command = br.readLine().toLowerCase();
						int giorno = new Integer(command);
						System.out.println("Inserisci il numero sessione (da 0 a "+(cong.getSessionPerDay()-1)+") ");
						command = br.readLine().toLowerCase();
						int sessione = new Integer(command);
						System.out.println("Inserisci il nome dello speaker... ");
						command = br.readLine().toLowerCase();
						String name = new String(command);
						System.out.println("Inserisci il cognome dello speaker... ");
						command = br.readLine().toLowerCase();
						String cognome = new String(command);
						cong.addSpeakerSession(giorno, sessione, new Speaker(name,cognome));
					break;
					case "get":
						System.out.println(cong.getCongressSchedule());
					break;
					case "exit":
						System.out.println("[Client] Esco...");
					break;
				}
			} while(!command.equalsIgnoreCase("exit"));
						
		} catch (RemoteException e) {
			System.out.println("Error in client: "+e.getMessage());
		} catch (NotBoundException e) {
			System.out.println("Remote object not found: "+e.getMessage());
		} catch (IOException e) {
			System.out.println("Error in input reading: "+e.getMessage());
		}
		
		System.out.println("[Client] Closed client!");
	}
		
}
