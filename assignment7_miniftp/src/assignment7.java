
public class assignment7 {

	public static void main(String args[])
	{
		// Avvio server di test
		Thread server = new Thread() {
			public void run()
			{
				// Porta 2000
				ServerFtp.main(new String[]{"2000"});
			}
		};
		server.start();
		
		// Avvio client
		ClientFtp.main(new String[]{"localhost","2000"});
		
		
	}
}
