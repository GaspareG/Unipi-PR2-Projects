package assignment4_ProConMensa;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class assignment4 {

	public static void main(String[] args) throws InterruptedException 
	{
		
		System.out.println(assignment4.getOrario() + " [Main] Avvio programma!");
		
		// Costanti hard-coded
		int ns = 100 ; // Numero studenti
		int np = 1 ;// Numero posti/piatti disponibili, non richiesto ma più elegante con
		int nl = 3 ;   // Numero lavatori
		int nr = 4 ;   // Numero risciacquatori
		
		// Oggetti in uso per la simulazione
		Mensa centrale = new Mensa("Mensa Cavalieri",np);
		Lavatore lav[] = new Lavatore[nl] ;
		Studente stud[] = new Studente[ns] ;
		Risciacquatore ris[] = new Risciacquatore[nr] ;
		
		// Creo e assegno i lavoratori alla mensa
		for(int i=0; i<lav.length; i++)
		{
			lav[i] = new Lavatore("Lavatore-"+i);
			centrale.add(lav[i]);
		}
		
		for(int i=0; i<ris.length; i++)
		{
			ris[i] = new Risciacquatore("Risciacquatore-"+i);
			centrale.add(ris[i]);
		}
		
		// Simulo l'arrivo di N studenti a intervalli casuali
		centrale.apri();
		for(int i=0; i<stud.length; i++)
		{
			stud[i] = new Studente("Studente-"+i);
			centrale.servi(stud[i]);
			Thread.sleep(250+(int)(Math.random()*1000));
		}

		// Aspetto la terminazione di tutti i thread creati
		System.out.println(assignment4.getOrario() + " [Main] Aspetto che tutti i thread siano terminati...");
		centrale.chiudi();	// Aspetta che tutti gli studenti siano usciti e tutti i piatti puliti
		System.out.println(assignment4.getOrario() + " [Main] Thread terminati, chiudo il programma!");
		
	}
	
	// Utility for printing current time in HH:mm:ss 
	public static String getOrario() 
	{ 
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SSS"); 
		Date date = new Date(); 
		return dateFormat.format(date) ; 
	}
}
