package assignment2_UfficioPostale;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class assignment2 {

	public static void main(String[] args) 
	{
	
		int k = 5 ;
		
		// parse and validate input from args
		if(args.length >= 1)
		{
			try 
			{
				int tempK = new Integer(args[0]);
				if(tempK > 0) k = tempK ;
			}
			catch(NumberFormatException c){ }
		}
		
		// Create a new Office
		UfficioPostale poste = new UfficioPostale(k);
		
		// Generate random task (person) for the post office
		int nPersone = 15 + (int)(Math.random()*16) ;
		for(int i = 0; i < nPersone; i++)
		{
			Persona tmp = new Persona( 3000 + (int)(Math.random()*3000), "Persona-"+i );
			poste.addTask(tmp);
		}
		
		// Start post-office operations
		poste.inizia();
		
	}

	// Utility for printing current time in HH:mm:ss
	public static String getOrario()
	{
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		Date date = new Date();
		return dateFormat.format(date) ;
	}
}
