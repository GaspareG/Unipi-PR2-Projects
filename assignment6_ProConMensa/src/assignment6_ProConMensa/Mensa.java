package assignment6_ProConMensa;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Mensa {
	
	private String nome ;
	private int numeroPosti ;
	private boolean aperta ;
	private BufferedWriter registro ;
	
	private BlockingQueue<Piatto> daLavare ;
	private BlockingQueue<Piatto> daSciacquare ;
	private BlockingQueue<Piatto> daUsare ;	

	private ArrayList<Lavatore> lavatori ;
	private ArrayList<Risciacquatore> risciacquatori ;

	private Queue<Thread> Tstudenti ;
	private Queue<Thread> Tlavatori ;
	private Queue<Thread> Trisciacquatori ;
	
	public Mensa(String nome, int numeroPosti, BufferedWriter registro) 
	{
		this.registro = registro ;
		
		this.nome = nome ;
		this.numeroPosti = numeroPosti ;
		this.daLavare = new LinkedBlockingQueue<Piatto>();
		this.daSciacquare = new LinkedBlockingQueue<Piatto>();
		this.daUsare = new LinkedBlockingQueue<Piatto>();
		
		this.lavatori = new ArrayList<Lavatore>();
		this.risciacquatori = new ArrayList<Risciacquatore>();
		
		this.aperta = false ;
		this.Tstudenti = new LinkedList<Thread>();
		this.Tlavatori = new LinkedList<Thread>();
		this.Trisciacquatori = new LinkedList<Thread>();
		
		// Creo i piatti da usare
		for(int i=0; i<numeroPosti; i++) this.daUsare.add(new Piatto("Piatto"+i));
		

	}
	public int getNumeroPosti() 
	{
		return numeroPosti;
	}
	
	public void apri() 
	{
		if(this.aperta == false)
		{
			// Avvio i thread lavatori/risciacquatori
			for(Lavatore l : this.lavatori)
			{
				l.assegnaMensa(this);
				Thread tl = new Thread(l);
				tl.start();
				this.Tlavatori.add(tl);
			}
			for(Risciacquatore r : this.risciacquatori)
			{
				r.assegnaMensa(this);
				Thread tr = new Thread(r);
				tr.start();
				this.Trisciacquatori.add(tr);
			}
			
			this.aperta = true ;
			System.out.println(assignment6.getOrario() + " ["+this.nome+"] Apro la mensa ["+this.getNumeroPosti()+" posti]!");
		}
	}
	public void chiudi() throws InterruptedException, IOException 
	{
		if(this.aperta == true)
		{
			this.aperta = false ;
			// Prima aspetto che tutti gli studenti terminino il pasto
			while(!this.Tstudenti.isEmpty())
			{
				Thread curr = this.Tstudenti.poll();
				curr.join();
			}
			// Dopo aspetto che tutti i lavatori abbiano lavato, svuotando la coda
			for(Lavatore l : this.lavatori) l.setTermina(true);
			while(!this.Tlavatori.isEmpty())
			{
				Thread curr = this.Tlavatori.poll();
				curr.join();
			}
			
			// Infine aspetto che tutti i risciacquatori abbiano pulito, svuotando la coda
			for(Risciacquatore r : this.risciacquatori) r.setTermina(true);
			while(!this.Trisciacquatori.isEmpty())
			{
				Thread curr = this.Trisciacquatori.poll();
				curr.join();
			}
			
			// Pulisco il buffer e chiudo il file
			this.registro.flush();
			this.registro.close();
			System.out.println(assignment6.getOrario() + " ["+this.nome+"] Chiudo la mensa!");
		}
	}
	
	public boolean isAperto()
	{
		return this.aperta ;
	}
	
	public void add(Lavatore lavatore) 
	{
		if(lavatore != null )
			this.lavatori.add(lavatore);
	}
	public void add(Risciacquatore risciacquatore) 
	{
		if(risciacquatore != null)
			this.risciacquatori.add(risciacquatore);
	}
	public void servi(Studente studente) 
	{
		if(studente == null ) return ;
		
		if(this.aperta == false)
		{
			System.out.println(assignment6.getOrario() + " ["+this.nome+"] Non posso servire ["+studente.getNome()+"]: mensa chiusa!");
			return ; 	// Non servo lo studente se mensa chiusa
		}
		else
		{
			System.out.println(assignment6.getOrario() + " ["+this.nome+"] Servo studente ["+studente.getNome()+"]");
			studente.setMensa(this);
			Thread t = new Thread(studente);
			t.start();
			this.Tstudenti.add(t);
		}
	}
	
	// Utilty per gli studenti
	public Piatto richiediPiatto() throws InterruptedException {
		return this.daUsare.take();
	}
	public void consegnaPiatto(Piatto daUsare) throws InterruptedException 
	{
		if(daUsare == null) return ;
		this.daLavare.put(daUsare);
	}

	// Utility per i lavatori
	public Piatto richiediPiattoDaLavare() throws InterruptedException {
		return this.daLavare.poll();
	}
	public void consegnaPiattoDaRisciacquare(Piatto daUsare) throws InterruptedException 
	{
		if(daUsare == null) return ;
		this.daSciacquare.put(daUsare);
	}
	
	// Utility per i risciacquatori
	public Piatto richiediPiattoDaRisciacquare() throws InterruptedException {
		return this.daSciacquare.poll();
	}
	public void consegnaPiattoDaUsare(Piatto daUsare) throws InterruptedException 
	{
		if(daUsare == null) return ;
		this.daUsare.put(daUsare);
	}
	
	public void aggiungiRegistro(Lavatore lavatore) throws IOException {
		if(this.registro== null) return ;
		this.registro.write(assignment6.getOrario()+","+lavatore.getNome()+","+lavatore.getCognome()+",lavatore,"+lavatore.getPiattiLavati());
		this.registro.newLine();
	}
	public void aggiungiRegistro(Risciacquatore risciacquatore) throws IOException {
		if(this.registro== null) return ;
		this.registro.write(assignment6.getOrario()+","+risciacquatore.getNome()+","+risciacquatore.getCognome()+",risciacquatore,"+risciacquatore.getPiattiLavati());
		this.registro.newLine();		
	}
	
}
