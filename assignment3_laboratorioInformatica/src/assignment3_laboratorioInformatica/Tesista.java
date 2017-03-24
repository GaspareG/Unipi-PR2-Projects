package assignment3_laboratorioInformatica;

import java.util.concurrent.locks.ReentrantLock;

public class Tesista implements Runnable, Utente {

	private String nome ;
	private Computer assegnato[] ;
	private int tempo ;
	private int idComputer ;
	
	public Tesista(String nome)
	{
		this.assegnato = null ;
		this.nome = nome ;
		this.tempo = 0 ;
		this.idComputer = 0 ;
	}
	
	@Override
	public void run() {
		// this.assegnato.length should be one
		assert(this.assegnato.length==1);
		ReentrantLock lock = this.assegnato[0].getLock() ;
		assert(lock != null && !lock.isLocked());
		lock.lock();
		System.out.println(assignment3.getOrario() + " ["+this.nome+"] Ha preso in uso il computer ["+this.assegnato[0].getNome()+"] per "+this.tempo+"ms");
		try {
			Thread.sleep(this.tempo);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		lock.unlock();
		System.out.println(assignment3.getOrario() + " ["+this.nome+"] Ha liberato l'uso del computer ["+this.assegnato[0].getNome()+"]");
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
		return this.idComputer ;
	}

	@Override
	public void setComputer(int pc) {
		this.idComputer = pc ;		
	}

	@Override
	public void assegnaComputers(int tempo, Computer[] pcs) {
		this.tempo = tempo ;
		this.assegnato = pcs ;
	}

}
