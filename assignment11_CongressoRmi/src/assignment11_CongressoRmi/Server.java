package assignment11_CongressoRmi;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server {

	public static int PORT = 2020 ;
	
	public static void main(String[] args) throws RemoteException {
		
		// 3 giorni, 12 sessioni a giorni, 5 speaker a sessione
		RCongresso cong = new RCongresso(3,12,5);

		for(int i=0; i<3; i++)
			for(int j=0; j<12; j++)
				cong.setSessionName(i, j, "Session-"+i+"-day-"+j);

		System.out.println("[Server] Created Congress!");
		System.out.println(cong.getCongressSchedule());
		
		try {
			System.out.println("[Server] Start SERVER....");
			//export object
			Congresso cStub=
			(Congresso) UnicastRemoteObject.exportObject(cong, 0);
			//register to RMI registry
			Registry registry= LocateRegistry.createRegistry(PORT);
			registry.rebind(Congresso.REMOTE_OBJECT_NAME, cStub);
			System.out.println("[Server] Finished server setup.");
		} catch (RemoteException e) {
			System.out.println("[Server] Error setting up RMI server: "+e.getMessage());
		}
	}

}
