package TwistServer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.List;

import TwistUtility.TwistAuthInterface;
import TwistUtility.TwistInviteInterface;
import TwistUtility.TwistJsonConf;


public class TwistServer {

	// Oggetto per la gestione dell'autenticazione
	private TwistAuth auth;

	// File JSON di configurazione
	private TwistJsonConf ServerConf ;
	private TwistJsonConf ScoreBoard ;
	private TwistJsonConf UsersData ;
	
	// Manager dei servizi
	private TwistOnline OnlineManager ;
	private TwistScoreboard ScoreboardManager ;	
	private TwistGame GameManager ;
	private TwistCleanUser CleanUser ;
	
	// Variabili di configurazione
	private Integer AuthRMIPort ;
	private Integer LoginTimeoutSec ;
	private Integer ScoreboardPort ;
	private String DictionaryFile;
	private Integer GamePort;
	
	// Thread avviati dal server
	private Thread CleanUserThread;
	private Thread ScoreboardThread;
	private Thread GameThread ;
	
	// Utility di gioco
	private List<String> DictionaryWords;
	private HashMap<String, TwistInviteInterface> callBackMap;
	
	public TwistServer()
	{
		System.out.println("[TwistServer] Avvio server...");

		// Carica i file di configurazione JSON
		System.out.println("[TwistServer] Carico file di configurazione!");
    	ServerConf = new TwistJsonConf(TwistServerStatic.sConfFileName);
    	ScoreBoard = new TwistJsonConf(TwistServerStatic.sScoreBoardFileName);
    	UsersData = new TwistJsonConf(TwistServerStatic.sUsersDataFileName);
		System.out.println("[TwistServer] file di configurazione caricati!");
    	
    	// Carica i dati dal json
		System.out.println("[TwistServer] Carico variabili di configurazione");
    	AuthRMIPort = ServerConf.getLong("AuthRMIPort").intValue();
    	LoginTimeoutSec = ServerConf.getLong("LoginTimeoutSec").intValue();
    	ScoreboardPort = ServerConf.getLong("ScoreboardPort").intValue();
    	DictionaryFile = ServerConf.getString("Dictionary");
    	GamePort = ServerConf.getLong("GamePort").intValue();
		System.out.println("[TwistServer] Variabili di configurazione caricate");
    	
    	// Carico i dati dal dizionario usando Java NIO
		System.out.println("[TwistServer] Carico dizionario parole");
    	Path path = Paths.get(DictionaryFile);
    	try
    	{
    		DictionaryWords = Files.readAllLines(path);
    	}
    	catch(IOException ex){
    		System.out.println("[TwistServer] Errore lettura dizionario");
    		ex.printStackTrace();
    	}
		System.out.println("[TwistServer] Dizionario parole caricato");
    	
    	// Gestori classifica, player online, partite
		System.out.println("[TwistServer] Creo i gestori di classifica, online, partite");
    	ScoreboardManager = new TwistScoreboard(ScoreBoard,ScoreboardPort);
    	OnlineManager = new TwistOnline(LoginTimeoutSec);
    	GameManager = new TwistGame(GamePort,DictionaryWords,OnlineManager,ScoreboardManager);
    			
    	// Avvio Thread di pulizia
		System.out.println("[TwistServer] Avvio thread pulizia player offline");
    	CleanUser = new TwistCleanUser(OnlineManager,LoginTimeoutSec);
    	CleanUserThread = new Thread(CleanUser);
    	CleanUserThread.start();
    	
    	// Avvio Thread Scoreboard
		System.out.println("[TwistServer] Avvio thread scoreboard");
    	ScoreboardThread = new Thread(ScoreboardManager);
    	ScoreboardThread.start();
    	
    	// Avvio Thread Gestore partite
		System.out.println("[TwistServer] Avvio thread gestore partite");
    	GameThread = new Thread(GameManager);
    	GameThread.start();
    	
    	// Mappa delle callback
    	callBackMap = new HashMap<String, TwistInviteInterface>();
    	
		// Registro via RMI Auth		
    	try {		
    		System.out.println("[TwistServer] Registro via RMI gestore autenticazione");
			Registry registry= LocateRegistry.createRegistry(AuthRMIPort);
			
        	auth = new TwistAuth(registry);
			auth.setUserFile(UsersData);
			auth.setOnlineManager(OnlineManager);
			auth.setCallBackMap(callBackMap);
			
			TwistAuthInterface cStub= (TwistAuthInterface) UnicastRemoteObject.exportObject(auth, 0);
			registry.rebind(TwistAuthInterface.REMOTE_OBJECT_NAME, cStub);
		} catch (RemoteException e) {
			System.out.println("[TwistServer] Error setting up RMI server: "+e.getMessage());
		}
	
    /*	while( true )
    	{
    		String users[] = OnlineManager.getOnlineUsers();
    		
    		for(int i=0; i<users.length; i++);
    		
    		try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    	}
    	*/
    	
    	try {
    		System.out.println("[TwistServer] Attendo termine dei thread");
			CleanUserThread.join();
	    	ScoreboardThread.join();
	    	GameThread.join();
		} catch (InterruptedException e) {
			
		}

		System.out.println("[TwistServer] Termino esecuzione server");
    	
	}
	public static void main(String[] args) 
	{

		new TwistServer();
	}

}
