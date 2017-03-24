import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class WebShopServer implements Runnable {

	private int porta ;
	private String nome ;
	private Vector<Prodotto> prodotti ;
	private ServerSocket ss ;
	
	public WebShopServer(int p, String n) {
		this.porta = p ;
		this.nome = n ;
		this.prodotti = new Vector<Prodotto>();
	}

	public void addProdotto(Prodotto p)
	{
		this.prodotti.addElement(p);
	}

	public void addProdotto(String nome, Float prezzo)
	{
		this.addProdotto(new Prodotto(nome,prezzo));
	}
	
	public int getPort()
	{
		return this.porta;
	}
	public String getNome()
	{
		return this.nome;
	}
	public String getUrl()
	{
		if(ss==null) return "" ;
		InetAddress addr = ss.getInetAddress();
		return addr == null ? "" : addr.getHostAddress();
	}
	
	@Override
	public void run() 
	{
		try 
		{
		
			System.out.println("[WebShopServer-"+nome+"] Avvio Server sulla porta "+porta+"...");
			ss = new ServerSocket();
			ss.bind(new InetSocketAddress(InetAddress.getLocalHost(), porta));
			ss.setReceiveBufferSize(4096);
			while(!Thread.interrupted())
			{
				System.out.println("[WebShopServer-"+nome+"] Aspetto client...");
				Socket ns = ss.accept();
				System.out.println("[WebShopServer-"+nome+"] Gestisco client");
				ns.setSendBufferSize(2048);
				ns.setTcpNoDelay(true);
				ns.setSoTimeout(200000);
				ObjectOutputStream out=new ObjectOutputStream(ns.getOutputStream());  	
				for(Prodotto p : prodotti)
				{
					out.writeObject(p);
					out.flush();
				}
				out.close();
				ns.close();
				System.out.println("[WebShopServer-"+nome+"] Termine gestione client");	
			}
			ss.close();
			System.out.println("[WebShopServer-"+nome+"] Termino Server");
		} catch (IOException e) {
			System.out.println("[WebShopServer-"+nome+"] IOException");			
			e.printStackTrace();
		}
	}
	
}
