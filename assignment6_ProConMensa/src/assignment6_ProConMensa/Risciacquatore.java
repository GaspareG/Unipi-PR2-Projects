package assignment6_ProConMensa;

import java.io.IOException;

public class Risciacquatore implements Runnable {

	private Mensa assegnato ;
	private String cognome ;
	private String nome ;
	private boolean termina ;
	private int piattiLavati;
	
	public Risciacquatore(String nome,String cognome) {
		this.nome = nome ;
		this.cognome = cognome ;
		this.termina = false ;
		this.piattiLavati = 0 ;
	}

	public boolean assegnaMensa(Mensa ass)
	{
		if(ass == null) return false ;
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
		
		try
		{

			Piatto daSciacquare = null ;
			// Se sono in chiusura e non ho piatti esco dal while
			while(true)
			{	
				daSciacquare = this.assegnato.richiediPiattoDaRisciacquare();
				if(daSciacquare != null)
				{
					this.piattiLavati++ ;
					System.out.println(assignment6.getOrario() + " ["+this.getNomeCognome()+"] Sciacquo piatto ["+daSciacquare.getNome()+"]!");
					
					// Simulo l'attesa
					Thread.sleep(1000+(int)(Math.random()*4000));
					this.assegnato.consegnaPiattoDaUsare(daSciacquare);	
				}
				else
				{
					if(this.termina) break ;
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
