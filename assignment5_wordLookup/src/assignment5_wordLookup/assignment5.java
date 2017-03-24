package assignment5_wordLookup;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class assignment5 {

	public static void main(String[] args) throws InterruptedException, IOException {
		
		int nThread = 8;
		BufferedReader buffer0 = new BufferedReader(new InputStreamReader(new FileInputStream("registro.txt")));
		BufferedReader buffer1 = new BufferedReader(new InputStreamReader(new FileInputStream("registro.txt")));
		BufferedReader buffer2 = new BufferedReader(new InputStreamReader(new FileInputStream("registro.txt")));
		BufferedReader buffer3 = new BufferedReader(new InputStreamReader(new FileInputStream("registro.txt")));

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Inserisci la parola da cercare... ");

        String daCercare = br.readLine();

		// Test case #0
		ParallelWordLookup pwl0 = new ParallelWordLookup(buffer0,nThread,daCercare);
		if(pwl0.getResult() != -1)
				System.out.println("["+assignment5.getOrario()+"] [Main] First occurency at line "+pwl0.getResult());
		System.out.println("");
		
		// Test case #1
		ParallelWordLookup pwl1 = new ParallelWordLookup(buffer1,nThread,"living"); // Riga 1  ~0sec
		if(pwl1.getResult() != -1)
			System.out.println("["+assignment5.getOrario()+"] [Main] First occurency at line "+pwl1.getResult());
		System.out.println("");
		

		// Test case #2
		ParallelWordLookup pwl2 = new ParallelWordLookup(buffer2,nThread,"sirbu"); // Riga 5600009 ~22sec
		if(pwl2.getResult() != -1)
			System.out.println("["+assignment5.getOrario()+"] [Main] First occurency at line "+pwl2.getResult());
		System.out.println("");
		

		// Test case #3
		ParallelWordLookup pwl3 = new ParallelWordLookup(buffer3,nThread,"errore"); // Not found ~22sec
		if(pwl3.getResult() != -1)
			System.out.println("["+assignment5.getOrario()+"] [Main] First occurency at line "+pwl3.getResult());
		System.out.println("");

		
	}
	
	// Utility for printing current time in HH:mm:ss:SSS
	public static String getOrario()
	{
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SSS");
		Date date = new Date();
		return dateFormat.format(date) ;
	}
}
