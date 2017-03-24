
import java.rmi.RemoteException;

public class assignment12 {

	public static void main(String[] args) throws InterruptedException {
		
			Thread server = new Thread() {
				public void run()
				{
					try {
						Server.main(new String[]{});
					} catch (RemoteException e) {
						System.out.println("Remote: "+e.getMessage());
					}
				}
			};
			server.start();
			
			Thread.sleep(1000);
			
			// Avvio client
			Client.main(new String[]{});
				
	}

}
