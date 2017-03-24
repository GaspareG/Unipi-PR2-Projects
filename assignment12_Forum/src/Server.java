import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server {

	public static int PORT = 2020 ;
	
	public static void main(String[] args) throws RemoteException {
		
		RForum forum = new RForum();
	
		System.out.println("[Server] Created forum!");
		
		try {
			System.out.println("[Server] Start SERVER....");
			//export object
			Forum cStub=
			(Forum) UnicastRemoteObject.exportObject(forum, 0);
			//register to RMI registry
			Registry registry= LocateRegistry.createRegistry(PORT);
			registry.rebind(Forum.REMOTE_OBJECT_NAME, cStub);
			System.out.println("[Server] Finished server setup.");
		} catch (RemoteException e) {
			System.out.println("[Server] Error setting up RMI server: "+e.getMessage());
		}
	}

}
