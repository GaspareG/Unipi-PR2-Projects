
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.concurrent.Callable;

public class PingServerTask implements Callable<Integer>{

	private DatagramSocket socket ;
	
	public PingServerTask(DatagramSocket server) {
		this.socket = server ;
	}
	public void print(String message)
	{
		System.out.println(Thread.currentThread().getName()+" " + message);
	}
	
	@Override
	public Integer call()  {
		
		DatagramPacket request = 
				new DatagramPacket(new byte[PingServer.MAX_LENGTH], PingServer.MAX_LENGTH);
		System.out.println("[PingServerTask] Server Task in ascolto...");
		while(!Thread.interrupted())
		{
			try
			{
				request.setLength(PingServer.MAX_LENGTH);
				socket.receive(request);
				
				
				
				boolean lost = PingServer.RND.nextFloat() < PingServer.PACKETLOSS ;
				long attesa = (((PingServer.RND.nextLong())%1000)+1000)%1000 ;
				// Converto messaggio
				byte bdata[] = request.getData() ;
				String msg = "" ;
				for(int i= 0; i<request.getLength()-1; i++)	msg += (char) bdata[i] ;
				
				// (PacketLoss)% di perdere il pacchetto
				if(lost)
				{
					System.out.println("[PingServerTask] ["+msg+"] Non inviato!");
					continue ;
				}
				// 75% attesa di  0-1s
				System.out.println("[PingServerTask] ["+msg+"] Ritardo "+attesa+"ms!");
				Thread.sleep(attesa);
				
				// Rimango il pacchetto
				DatagramPacket response = 
						new DatagramPacket(request.getData(), request.getLength(),request.getSocketAddress());
				socket.send(response);
			} 
			catch (IOException e) 
			{	
				System.out.println("[PingServerTask] Errore socket send: "+e.getMessage());
			} 
			catch (InterruptedException e) {
				System.out.println("[PingServerTask] Thread interrotto "+e.getMessage());
			}
		}
		return 0 ;
	}

}
