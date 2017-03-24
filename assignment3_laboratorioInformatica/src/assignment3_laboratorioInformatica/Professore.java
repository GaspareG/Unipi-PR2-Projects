package assignment3_laboratorioInformatica;

import java.util.concurrent.locks.ReentrantLock;

public class Professore implements Utente, Runnable {

	private String nome ;
	private Computer assegnato[] ;
	private int tempo ;
	
	public Professore(String nome)
	{
		this.assegnato = null ;
		this.nome = nome ;
		this.tempo = 0 ;
	}
	
	@Override
	public void run() {
		for(int i=1; i<this.assegnato.length; i++)
		{
			ReentrantLock lock = this.assegnato[i].getLock() ;
			assert(lock != null && !lock.isLocked());
			lock.lock();
		}
		System.out.println(assignment3.getOrario() + " ["+this.nome+"] Ha preso tutti i computer per "+this.tempo+"ms");
		try {
			Thread.sleep(this.tempo);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		for(int i=1; i<this.assegnato.length; i++)
		{
			ReentrantLock lock = this.assegnato[i].getLock() ;
			lock.unlock();
		}		
		System.out.println(assignment3.getOrario() + " ["+this.nome+"] Ha terminato il suo lavoro e libera tutti i pc");
	}

	@Override
	public String getNome() {
		return this.nome ;
	}

	@Override
	public void setNome(String n) {
		this.nome = n ;		
	}

	@Override
	public int getComputer() {
		return 0;	// I professori hanno sempre bisogno di tutti i PC
	}

	@Override
	public void setComputer(int pc) {
		// Non fa niente
	}

	@Override
	public void assegnaComputers(int tempo, Computer[] pcs) {
		this.tempo = tempo ;
		this.assegnato = pcs ;
	}

}
