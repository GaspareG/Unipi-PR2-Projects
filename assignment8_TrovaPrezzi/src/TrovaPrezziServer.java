import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TrovaPrezziServer implements Runnable {

	private Thread refreshThread ;
	private int port ;
	private int refreshTime ;
	private Vector<String> shopUrl ;
	private Vector<Integer> shopPort ;
	private Vector<String> shopName ;
	private Vector<Item> items ;
	public TrovaPrezziServer(int p, int to) 
	{
		this.port = p ;
		this.refreshTime = to ;
		this.shopPort = new Vector<Integer>();
		this.shopUrl = new Vector<String>();
		this.shopName = new Vector<String>();
		this.items = new Vector<Item>();
		
		this.refreshThread = new Thread() {
			public void run()
			{
				System.out.println("[TrovaPrezziServer] Chiusa thread aggiornamento prezzi");
				Vector<Item> nuovoListino = new Vector<Item>();
				for(int i = 0 ; i < shopUrl.size(); i++)
				{
					Socket conn = new Socket();
					InetSocketAddress server = new InetSocketAddress(shopUrl.get(i),shopPort.get(i));
					conn = new Socket();
					try {
						conn.connect(server);
						ObjectInputStream in=new ObjectInputStream(conn.getInputStream());  
						while(!conn.isClosed())
						{
							Prodotto trovato = (Prodotto) in.readObject();
							if(trovato == null) return ;
							System.out.println("Aggiunto "+trovato.toString());
							nuovoListino.addElement(new Item(trovato.getNome(),trovato.getPrezzo(),shopName.get(i)));
						}
						conn.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
					}

					
				}
				items = nuovoListino ;
				
				while(!Thread.interrupted())
				{
					try {
						Thread.sleep(refreshTime);
					} catch (InterruptedException e) {
						System.out.println("[TrovaPrezziServer] Thread aggiornamento prezzi terminato");
					}
					System.out.println("[TrovaPrezziServer] Aggiorno prezzi...");
				}
			}
			
		};
		this.refreshThread.start();
	}


	@Override
	public void run() 
	{
		ServerSocket ss = null ;
		ExecutorService es = null ;
		try {
			ss = new ServerSocket();
			ss.bind(new InetSocketAddress(InetAddress.getLocalHost(), this.port));
			ss.setReceiveBufferSize(4096);
			
			es = Executors.newFixedThreadPool(4);

			System.out.println("[TrovaPrezziServer] In ascolto...");
			while(!Thread.interrupted())
			{
				Socket ns = ss.accept();
				System.out.println("[TrovaPrezziServer] Gestisco ["+ns.getInetAddress().getHostAddress()+"]");
				TrovaPrezziServerTask task = new TrovaPrezziServerTask(ns,this);
				es.execute(task);
			}
			ss.close();
			System.out.println("[TrovaPrezziServer] termiono server...");
		} catch (IOException e) {
			if(es != null) es.shutdownNow();
		}

	}

	public void addShop(WebShopServer negozio) {
		this.shopUrl.addElement(negozio.getUrl());
		this.shopPort.addElement(negozio.getPort());
		this.shopName.addElement(negozio.getNome());
	}
	
	public void addShop(String url,int port,String nome) {
		this.shopUrl.addElement(url);
		this.shopPort.addElement(port);
		this.shopName.addElement(nome);
	}

	public Vector<Item> getListino()
	{
		return this.items;
	}
}
