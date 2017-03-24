package assignment6_ProConMensa;

import java.util.concurrent.atomic.AtomicReference;

/*
 * Oggetto Piatto, controlla e imposta i vari stati in maniera atomica
 */
public class Piatto {
	private AtomicReference<String> stato ;
	private AtomicReference<String> utilizzatore ;
	private String nome ;
	
	public Piatto(String nome)
	{
		this.nome = nome ;
		this.stato = new AtomicReference<String>("pulito");
		this.utilizzatore = new AtomicReference<String>("-");
	}
	
	public String getNome()
	{
		return this.nome ;
	}
	
	public boolean usa(String nome)
	{
		boolean r = true ;
		if(nome == null) r = false ;
		else 
		{
			r = this.stato.compareAndSet("pulito", "sporco");
			if(r)
			{
				this.utilizzatore.set(nome);		
			}
		}
		
		return r ;
	}
	
	public boolean lava()
	{
		return this.stato.compareAndSet("sporco", "lavato");
	}
	
	public boolean sciacqua()
	{
		return this.stato.compareAndSet("lavato", "pulito");
	}
	
	public String getStato()
	{
		return this.stato.get();
	}
	
	public String getUtilizzatore()
	{
		return this.utilizzatore.get();
	}
}
