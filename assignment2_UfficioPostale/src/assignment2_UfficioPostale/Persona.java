package assignment2_UfficioPostale;

public class Persona implements Runnable {

	private int tempoOperazione ;
	private String nome ;
		
	public Persona(int tempoOperazione, String nome) {
		super();
		this.tempoOperazione = tempoOperazione;
		this.nome = nome;
	}

	@Override
	public void run() {
		System.out.println("["+assignment2.getOrario()+"] ["+this.nome+"] Inizio operazione allo sportello... (tempo "+this.tempoOperazione+"ms)");
		try {
			Thread.sleep(this.tempoOperazione);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("["+assignment2.getOrario()+"] ["+this.nome+"] Operazione conclusa, esco dall'ufficio!");
	}

}
