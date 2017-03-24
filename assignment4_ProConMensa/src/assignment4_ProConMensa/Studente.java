package assignment4_ProConMensa;

public class Studente implements Runnable {

	private String nome ;
	private Mensa mensa ;
	
	public Studente(String nome) {
		this.nome = nome ;
	}

	public void setMensa(Mensa mensa)
	{
		this.mensa = mensa ;
	}
	public String getNome()
	{
		return this.nome;
	}
	@Override
	public void run() {
			Piatto daUsare = null ;
			
			try {
				daUsare = this.mensa.richiediPiatto();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if(daUsare == null) return ;
			
			System.out.println(assignment4.getOrario() + " ["+this.nome+"] Uso piatto ["+daUsare.getNome()+"]!");
			daUsare.usa(this.nome);
			
			// Simulo l'attesa per il pasto
			try {
				Thread.sleep(1000+(int)(Math.random()*4000));
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			System.out.println(assignment4.getOrario() + " ["+this.nome+"] Consegno piatto ["+daUsare.getNome()+"] ed esco!");
			
			try {
				this.mensa.consegnaPiatto(daUsare);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}

}
