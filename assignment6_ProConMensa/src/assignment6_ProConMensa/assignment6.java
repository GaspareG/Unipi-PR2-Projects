package assignment6_ProConMensa;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class assignment6 {

	public static void main(String[] args) throws InterruptedException, IOException 
	{
		
		System.out.println(assignment6.getOrario() + " [Main] Avvio programma!");
		
		// Costanti hard-coded
		int ns = 100 ; // Numero studenti
		int np = 1000 ;// Numero posti/piatti disponibili, non richiesto ma più elegante con
		int nl = 3 ;   // Numero lavatori
		int nr = 4 ;   // Numero risciacquatori
		
		// Oggetti in uso per la simulazione
		BufferedWriter registro =  new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream("registro.txt",true),"UTF-8"));
		Mensa centrale = new Mensa("Mensa Cavalieri",np,registro);
		Lavatore lav[] = new Lavatore[nl] ;
		Studente stud[] = new Studente[ns] ;
		Risciacquatore ris[] = new Risciacquatore[nr] ;
		
		// Creo e assegno i lavoratori alla mensa
		String[] lavNome = new String[]{"Marco","Alessio","Gaspare"};
		String[] lavCognome = new String[]{"Rossi","Verdi","Bianchi"};
		for(int i=0; i<lav.length; i++)
		{
			lav[i] = new Lavatore(lavNome[i],lavCognome[i]);
			centrale.add(lav[i]);
		}

		String[] risNome = new String[]{"Giuseppe","Alessandro","Davide","Francesco"};
		String[] risCognome = new String[]{"Parodi","Brambilla","Fumagalli","Ferrari"};
		for(int i=0; i<ris.length; i++)
		{
			ris[i] = new Risciacquatore(risNome[i],risCognome[i]);
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
		System.out.println(assignment6.getOrario() + " [Main] Aspetto che tutti i thread siano terminati...");
		centrale.chiudi();	// Aspetta che tutti gli studenti siano usciti e tutti i piatti puliti
		System.out.println(assignment6.getOrario() + " [Main] Thread terminati, chiudo il programma!");
		
		// Stampo i pagamenti
		System.out.println("");
		RegistroPagamenti pagamenti = new RegistroPagamenti("registro.txt");
		pagamenti.stampaPagamenti();
		
	}
	
	// Utility for printing current time in HH:mm:ss 
	public static String getOrario() 
	{ 
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SSS"); 
		Date date = new Date(); 
		return dateFormat.format(date) ; 
	}
}
