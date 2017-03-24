package assignment3_laboratorioInformatica;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

public class assignment3 {

	public static void main(String[] args) throws InterruptedException {
		
		int k = 20 ; // Number of computers
		
		// Create Lab with Tutor
		Tutor tutore = new Tutor("Tutor Topolino");
		Laboratorio info = new Laboratorio(k);
		
		info.setTutor(tutore);
		
		// Generate Random Prof/PhD Student/CS Student
		Utente lista[] = new Utente[50] ;
		int p=0,s=0,t=0; // index per naming
		for(int i=0; i<lista.length; i++)
		{
			double rnd = Math.random();
			// 15% Prof | 25% Tesisti | 60% Studenti
			if(rnd <= 0.15)	
			{
				lista[i] = new Professore("Prof-"+(++p));
			}
			else if(rnd <= 0.4) 
			{
				lista[i] = new Tesista("Tesista-"+(++t));
				lista[i].setComputer( 1+(int)(Math.random()*k));
			}
			else
			{
				lista[i] = new Studente("Studente-"+(++s));
			}
			System.out.println("[MAIN] Utente["+i+"] ["+lista[i].getNome()+"] idComputer["+lista[i].getComputer()+"]");
		}
		
		// Open Computer Lab, Tutor start to assign Users to Computers...
		info.apriLaboratorio();
		

		// Simulate arrival of Users... (each user enter three times in the lab)
		for(int i=0; i< 3; i++)
		{
			// Random shuffle list of users...
			Collections.shuffle(Arrays.asList(lista));
			// Send User to the lab
			for(int j=0; j<lista.length; j++)
			{
				info.richiesta(lista[j]);
				Thread.sleep((int)(Math.random()*1000));
			}
		}
		info.chiudiLaboratorio();
		
	}
	
	// Utility for printing current time in HH:mm:ss
	public static String getOrario()
	{
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		Date date = new Date();
		return dateFormat.format(date) ;
	}
}
