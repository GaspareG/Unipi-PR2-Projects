
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Vector;

public interface Forum extends Remote {
	
	public final static String REMOTE_OBJECT_NAME="FORUM";

	// Creazione di un nuovo forum
	public void addForum(String forumName)
			throws RemoteException;

	// Aggiunta di un nuovo messaggio ad un forum esistente
	public void addMessage(String forumName, String message)
			throws RemoteException;
	
	// Aggiunta di un nuovo messaggio ad un forum esistente
	public Vector<String> getMessage(String forumName)
			throws RemoteException;
	
	// Registrazione ad un forum
	public void subForum(String forumName, Notifica not)
			throws RemoteException;
	
}