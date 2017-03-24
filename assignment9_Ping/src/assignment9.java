
public class assignment9 {

	public static void main(String[] args) throws InterruptedException {

		// Creo Ping Server
		System.out.println("[Main] Avvio PingServer...");
		
		Thread server = new Thread() {
			public void run()
			{
				// Porta 2000, seed 42, 30% packet loss
				PingServer.main(new String[]{"2000","42","30"});
			}
		};
		
		server.start();

		// Avvio client
		System.out.println("[Main] Avvio PingClient...");
		PingClient.main(new String[]{"localhost","2000"});
		
		server.interrupt();
	}

}
