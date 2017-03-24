package assignment4_ProConMensa;

public class Lavatore implements Runnable {

	private Mensa assegnato ;
	private String nome ;
	
	public Lavatore(String nome) {
		this.nome = nome ;
	}

	public boolean assegnaMensa(Mensa ass)
	{
		if(this.assegnato != null) return false ;
		this.assegnato = ass ;
		return true ;
	}
	
	@Override
	public void run() {
		System.out.println(assignment4.getOrario() + " ["+this.nome+"] Inizio lavoro!");
		
		try 
		{
			while(!Thread.currentThread().isInterrupted())
			{
				Piatto daLavare = null ;
				
				daLavare = this.assegnato.richiediPiattoDaLavare() ;
								
				if(daLavare != null)
				{
					System.out.println(assignment4.getOrario() + " ["+this.nome+"] Lavo piatto ["+daLavare.getNome()+"]!");
					
					// Simulo l'attesa
					Thread.sleep(1000+(int)(Math.random()*4000));
					this.assegnato.consegnaPiattoDaRisciacquare(daLavare);
					
				}
			}
		}
		catch(InterruptedException e)
		{		
			e.printStackTrace();
		}	

		System.out.println(assignment4.getOrario() + " ["+this.nome+"] Ho terminato il mio lavoro, esco!");
		this.assegnaMensa(null);	
		
	}

}
