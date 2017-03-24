package assignment6_ProConMensa;

import java.io.IOException;

public class Lavatore implements Runnable {

	private Mensa assegnato ;
	private String cognome ;
	private String nome ;
	private boolean termina ;
	private int piattiLavati;
	
	public Lavatore(String nome,String cognome) {
		this.nome = nome ;
		this.cognome = cognome ;
		this.termina = false ;
		this.piattiLavati = 0 ;
	}

	public boolean assegnaMensa(Mensa ass)
	{
		if(this.assegnato != null) return false ;
		this.assegnato = ass ;
		return true ;
	}
	
	public String getNomeCognome()
	{
		return this.nome + " " + this.cognome ;
	}
	
	
	public String getCognome() {
		return cognome;
	}

	public String getNome() {
		return nome;
	}

	public int getPiattiLavati() {
		return piattiLavati;
	}


	public void setTermina(boolean termina) {
		this.termina = termina;
	}

	@Override
	public void run() {
		System.out.println(assignment6.getOrario() + " ["+this.getNomeCognome()+"] Inizio lavoro!");

		this.termina = false ;
		
		try 
		{
			Piatto daLavare = null ;
			// Se sono in chiusura e non ho piatti esco dal while
			while( true )
			{		
				daLavare = this.assegnato.richiediPiattoDaLavare();
				
				if(daLavare != null)
				{
					this.piattiLavati++ ;
					System.out.println(assignment6.getOrario() + " ["+this.getNomeCognome()+"] Lavo piatto ["+daLavare.getNome()+"]!");
					
					// Simulo l'attesa
					Thread.sleep(1000+(int)(Math.random()*4000));
					this.assegnato.consegnaPiattoDaRisciacquare(daLavare);
					
				}
				else
				{
					if( this.termina ) break ;
					Thread.sleep(200);
				}
			}
			System.out.println(assignment6.getOrario() + " ["+this.getNomeCognome()+"] Ho terminato il mio lavoro, esco!");
			this.assegnato.aggiungiRegistro(this);
			this.assegnaMensa(null);	
		}
		catch(InterruptedException e)
		{		
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	

		
	}

}
