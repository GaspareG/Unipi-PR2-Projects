package assignment4_ProConMensa;

public class Risciacquatore implements Runnable {
	
	private Mensa assegnato ;
	private String nome ;
	
	public Risciacquatore(String nome) {
		this.nome = nome ;
	}

	public boolean assegnaMensa(Mensa ass)
	{
		if(ass == null) return false ;
		if(this.assegnato != null) return false ;
		this.assegnato = ass ;
		return true ;
	}
	
	@Override
	public void run() {
		System.out.println(assignment4.getOrario() + " ["+this.nome+"] Inizio lavoro!");
		
		try
		{

			Piatto daSciacquare = null ;
			
			while( true )
			{
				daSciacquare = this.assegnato.richiediPiattoDaRisciacquare() ;
				
				if(daSciacquare != null)
				{
					System.out.println(assignment4.getOrario() + " ["+this.nome+"] Sciacquo piatto ["+daSciacquare.getNome()+"]!");
					
					// Simulo l'attesa
					Thread.sleep(1000+(int)(Math.random()*4000));
					this.assegnato.consegnaPiattoDaUsare(daSciacquare);
					
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
