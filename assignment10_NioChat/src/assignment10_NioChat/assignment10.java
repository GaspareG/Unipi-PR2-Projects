package assignment10_NioChat;

public class assignment10 {

	public static void main(String[] args) {
		
		// Creo Ping Server
		System.out.println("[Main] Avvio Chat Server...");
		
		Thread server = new Thread() {
			public void run()
			{
				ChatServer.main(new String[]{"2000"});
			}
		};
		
		server.start();

		// Avvio client
		System.out.println("[Main] Avvio Chat Client...");
		ChatClient.main(new String[]{"localhost","2000"});

		System.out.println("[Main] Chat Client terminato, chiudo chat Server...");
		try {
			server.interrupt();
			server.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println("[Main] Chat server chiuso, termino...");
	}

}
