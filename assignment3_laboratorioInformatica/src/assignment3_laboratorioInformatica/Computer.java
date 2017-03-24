package assignment3_laboratorioInformatica;

import java.util.concurrent.locks.ReentrantLock;

public class Computer {
	private String nome ;
	private ReentrantLock locker ;
	
	public Computer(String nome) {
		this.nome = nome ;
		locker = new ReentrantLock();
	}
	
	public ReentrantLock getLock()
	{
		return this.locker ;
	}

	public String getStatus()
	{
		return "["+this.nome+"] Stato:" + ( this.locker.isLocked() ? "in uso" : "libero");
	}
	
	public String getNome()
	{
		return this.nome ;
	}
}
