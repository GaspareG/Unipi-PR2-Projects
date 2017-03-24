package assignment3_laboratorioInformatica;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class Tutor implements Runnable {

	private String nome ;
	private Laboratorio daGestire ;
	private Queue<Thread> QThread ;
	
	public Tutor(String string) {
		this.nome = string ;
		this.daGestire = null ;
		this.QThread = new LinkedList<Thread>();
	}

	
	@Override
	public void run() {

		if(this.daGestire == null)
		{
			System.out.println(assignment3.getOrario() + " ["+this.nome+"] Nessun laboratorio da gestire, termino...");
			return ;
		}
		Queue<Professore> QP = this.daGestire.getQProf() ;
		Queue<Tesista> QT = this.daGestire.getQTesista() ;
		Queue<Studente> QS = this.daGestire.getQStudente() ;
		Computer pc[] = this.daGestire.getPc();
		
		System.out.println(assignment3.getOrario() + " ["+this.nome+"]\t\tApro il laboratorio e inizio a gestire le code");
		
		// Thread is stopped when receive interruption signal and all the queue are empty
		while( !(!this.daGestire.isOpen() && QP.isEmpty() && QT.isEmpty() && QS.isEmpty()) )
		{
			
			boolean freePc[] = new boolean[pc.length] ;
			int nfree = 0 ;
			for(int i=1; i<pc.length; i++)
			{
				freePc[i] = !pc[i].getLock().isLocked() ;
				if(freePc[i]) nfree++ ;
			}

			System.out.println(assignment3.getOrario() + " ["+this.nome+"]\t\t prof["+QP.size()+"] tesisti["+QT.size()+"] studenti["+QS.size()+"] Computer ["+nfree+"/"+(pc.length-1)+"]");
			
			// First professors
			if(QP.size() != 0)
			{
				if(nfree==freePc.length-1)
				{
					Professore nuovoTask = QP.poll();
					nuovoTask.assegnaComputers(1000+(int)(Math.random()*5000), pc); // Assegno tutti i pc
					this.start(nuovoTask);
					nfree = 0 ;
				}
			}
			// Then PhD students & normal students
			else
			{
				// PhDs
				Iterator<Tesista> it = QT.iterator();
				Queue<Tesista> toRemove = new LinkedList<Tesista>();
				
				while(it.hasNext())
				{
					Tesista curr = it.next();
					int pcId = curr.getComputer();
					if(freePc[pcId])
					{
						freePc[pcId] = false ;
						nfree-- ;
						curr.assegnaComputers(1000+(int)(Math.random()*2500), new Computer[]{pc[curr.getComputer()]});
						this.start(curr);
						toRemove.add(curr);
					}
				}
				// Clean PhD Students arraylist
				while(!toRemove.isEmpty()) QT.remove(toRemove.poll());
					
				// Students
				while(nfree>0 && !QS.isEmpty())
				{
					Studente curr = QS.poll();
					int i = 0;
					while(!freePc[i]) i++;
					curr.assegnaComputers(1000+(int)(Math.random()*1000), new Computer[]{pc[i]});
					nfree-- ;
					freePc[i] = false ;
					this.start(curr);
				}
			}
			
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			};
			
		}
		
		// Waiting for Threads still alive
		while(!this.QThread.isEmpty())
		{
			Thread curr = this.QThread.poll();
			try {
				curr.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		System.out.println(assignment3.getOrario() + " ["+this.nome+"]\t\tChiudo il laboratorio...");
		this.setLaboratorio(null);
	}


	private void start(Professore nuovoTask) {
		System.out.println(assignment3.getOrario() + " ["+this.nome+"]\t\tAvvio task per ["+nuovoTask.getNome()+"]");
		Thread tutente = new Thread(nuovoTask);
		this.QThread.add(tutente);
		tutente.start();
	}
	private void start(Tesista nuovoTask) {
		System.out.println(assignment3.getOrario() + " ["+this.nome+"]\t\tAvvio task per ["+nuovoTask.getNome()+"]");
		Thread tutente = new Thread(nuovoTask);
		this.QThread.add(tutente);
		tutente.start();
	}
	private void start(Studente nuovoTask) {
		System.out.println(assignment3.getOrario() + " ["+this.nome+"]\t\tAvvio task per ["+nuovoTask.getNome()+"]");
		Thread tutente = new Thread(nuovoTask);
		this.QThread.add(tutente);
		tutente.start();
	}


	public void setLaboratorio(Laboratorio laboratorio) {
		this.daGestire = laboratorio ;
	}

}
