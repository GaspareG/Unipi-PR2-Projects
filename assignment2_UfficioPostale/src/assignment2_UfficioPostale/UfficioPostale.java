package assignment2_UfficioPostale;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UfficioPostale {

	private int numeroSportelli ;
	private Queue<Persona> Q ;
	
	private ExecutorService executor ;
	
	public UfficioPostale(int k)
	{
		this.numeroSportelli = k ;
		this.Q = new LinkedList<Persona>();
	}
	
	public void addTask(Persona tmp) {
		this.Q.add(tmp);
	}

	public void inizia() {
		System.out.println("["+assignment2.getOrario()+"] [UfficioPostale] Apro l'ufficio! Inizio a servire i task! ("+this.Q.size()+" task in attesa) ("+this.numeroSportelli+" sportelli attivi)");
		this.executor = Executors.newFixedThreadPool(this.numeroSportelli);
		while( !this.Q.isEmpty() )
		{
			this.executor.execute(this.Q.poll());
		}
		this.executor.shutdown();
		while( !this.executor.isTerminated() ) {}
		System.out.println("["+assignment2.getOrario()+"] [UfficioPostale] Task terminati! Chiudo l'ufficio!");
		
	}
}
