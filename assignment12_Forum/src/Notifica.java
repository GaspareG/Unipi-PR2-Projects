import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Notifica extends Remote {

	public String getName()
			throws RemoteException;
	
	public void sendNotify(String forum, String msg)
			throws RemoteException;
}
