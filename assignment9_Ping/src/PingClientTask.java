
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.util.concurrent.Callable;

public class PingClientTask implements Callable<Long>{
	
	private DatagramSocket client ;
	private SocketAddress serverSocket ;
	private int number ;
	private InetAddress address ;
	public PingClientTask(DatagramSocket client, SocketAddress serverSocket, InetAddress addr, int i) {
		this.client = client ;
		this.serverSocket = serverSocket ;
		this.number = i ;
		this.address = addr ;
	}

	@Override
	public Long call() throws Exception {
		
		Long currTS = System.currentTimeMillis();
		Long ret ;
		String message = "PING "+this.number+" "+ currTS ;
		
		byte[] bmessage = message.getBytes();
		DatagramPacket request  = new DatagramPacket(bmessage, bmessage.length,this.serverSocket);
		DatagramPacket response = new DatagramPacket(bmessage, bmessage.length,this.serverSocket);
		
		// Imposto timeout risposta e invia richiesta
		client.setSoTimeout(2000);
		client.send(request);
		
		try {
	        client.receive(response);
	        ret = new Long(System.currentTimeMillis()-currTS);
	        
	        System.out.println(response.getLength() + " bytes from "+address.getHostName()+" ("+address.getHostAddress()+") seq="+number+" time="+ret+"ms");
			
	    } catch (SocketTimeoutException e) {
	    	System.out.println("Request timed out");
	    	ret = new Long(-1);
	    }

		return ret ;
	}

}
