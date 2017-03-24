import java.util.Random;

public class assignment8 
{
	public static void main(String[] args) 
	{
		WebShopServer negozio1 = new WebShopServer(2000,"Amazon");
		WebShopServer negozio2 = new WebShopServer(2001,"Ebay");
		WebShopServer negozio3 = new WebShopServer(2002,"GearBest");
		
		// Inserisci i prodotti
		Random rnd = new Random(42);
		String nomiProdotti[] = new String[]{"Portatile","Smartphone","Tv","Processore","Mouse","Tastiera"};
		Float maxPrezzo[] = new Float[]{600.f,550.f,1200.f,250.f,50.f,20.f};
		for(int i=0; i<nomiProdotti.length; i++)
		{
			negozio1.addProdotto(nomiProdotti[i],rnd.nextFloat()*maxPrezzo[i]);
			negozio2.addProdotto(nomiProdotti[i],rnd.nextFloat()*maxPrezzo[i]);
			negozio3.addProdotto(nomiProdotti[i],rnd.nextFloat()*maxPrezzo[i]);
		}
		
		Thread tn1 = new Thread(negozio1);
		Thread tn2 = new Thread(negozio2);
		Thread tn3 = new Thread(negozio3);

		tn1.start();
		tn2.start();
		tn3.start();

		// Server, porta 2005, refresh 30secondi
		TrovaPrezziServer tps = new TrovaPrezziServer(2005,30*1000);
		tps.addShop(negozio1);
		tps.addShop(negozio2);
		tps.addShop(negozio3);
		Thread ttps = new Thread(tps);
		ttps.start();
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		TrovaPrezziClient cliente = new TrovaPrezziClient("127.0.0.1",2005);
		Thread tc = new Thread(cliente);
		tc.start();
		
		try 
		{
			tc.join();
			
			ttps.interrupt();
			tn1.interrupt();
			tn2.interrupt();
			tn3.interrupt();

			ttps.join();
			tn1.join();
			tn2.join();
			tn3.join();
		} 
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
	}
}
