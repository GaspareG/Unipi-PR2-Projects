package assignment3_laboratorioInformatica;

import java.util.LinkedList;
import java.util.Queue;

public class Laboratorio {

	private Computer pc[] ;
	private Tutor responsabile ;
	
	private Queue<Professore> QProf ;
	private Queue<Tesista> QTesista ;
	private Queue<Studente> QStudente ;
	private Thread threadTutor ;
	private boolean open;
	
	public Laboratorio(int k) {
		this.responsabile = null ;
		this.QProf = new LinkedList<Professore>();
		this.QStudente = new LinkedList<Studente>();
		this.QTesista = new LinkedList<Tesista>();
		this.threadTutor = null ;
		this.open = false ;
		pc = new Computer[k+1] ;
		pc[0] = null ;
		for(int i=1; i<=k; i++)
			pc[i] = new Computer("Computer-"+i);
	}

	public void setTutor(Tutor nuovo)
	{
		this.responsabile = nuovo ;
	}
	
	public void richiesta(Utente u)
	{
		if(u instanceof Professore) this.richiesta((Professore)u);
		else if(u instanceof Tesista) this.richiesta((Tesista)u);
		else this.richiesta((Studente)u);
	}
	public void richiesta(Professore p)
	{
		//System.out.println(assignment3.getOrario() + " [Laboratorio] Prof. [" + p.getNome() + "] in coda...");
		this.QProf.add(p);
	}
	public void richiesta(Tesista t)
	{
		//System.out.println(assignment3.getOrario() + " [Laboratorio] Tesista [" + t.getNome() + "] in coda...");
		this.QTesista.add(t);
	}
	public void richiesta(Studente s)
	{
		//System.out.println(assignment3.getOrario() + " [Laboratorio] Studente [" + s.getNome() + "] in coda...");
		this.QStudente.add(s);
	}
	
	public boolean apriLaboratorio()
	{
		if(this.responsabile == null) return false ;
		this.open = true ;
		this.responsabile.setLaboratorio(this);
		this.threadTutor = new Thread(this.responsabile);
		this.threadTutor.start();
		return true ;
	}
	
	public boolean chiudiLaboratorio()
	{
		this.open = false ;
		return true ;
	}

	public Computer[] getPc() {
		return pc;
	}

	public Queue<Professore> getQProf() {
		return QProf;
	}

	public Queue<Tesista> getQTesista() {
		return QTesista;
	}

	public Queue<Studente> getQStudente() {
		return QStudente;
	}

	public boolean isOpen() {
		return this.open ;
	}
	
	
}
